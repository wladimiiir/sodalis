
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.category;

import scala.collection.JavaConversions;
import sk.magiksoft.sodalis.category.entity.Categorized;
import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.category.entity.CategoryNodeWrapper;
import sk.magiksoft.sodalis.category.entity.DynamicCategory;
import sk.magiksoft.sodalis.category.report.CategoryPathWrapper;
import sk.magiksoft.sodalis.category.ui.CategoryUI;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.context.AbstractContextManager;
import sk.magiksoft.sodalis.core.context.Context;
import sk.magiksoft.sodalis.core.data.remote.server.DataManager;
import sk.magiksoft.sodalis.core.factory.EntityFactory;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.module.Module;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author wladimiiir
 */
public class CategoryManager extends AbstractContextManager {
    public static final Long UNCATEGORIZED_ID = -1111111l;
    private static CategoryManager instance = null;

    private CategoryManager() {
        instance = this;
    }

    @Override
    protected Context createContext() {
        return new CategoryUI();
    }

    @Override
    protected boolean isFullTextActive() {
        return false;
    }

    public static synchronized CategoryManager getInstance() {
        if (instance == null) {
            new CategoryManager();
        }

        return instance;
    }

    public Category getRootCategory(Class<? extends Module> moduleClass, boolean includeDynamicCategories) {
        final Module module = SodalisApplication.get().getModuleManager().getModuleByClass(moduleClass);
        if (module == null) {
            return null;
        }
        final Category moduleCategory = CategoryDataManager.getInstance().getModuleCategory(module);
        Category category;

        if (moduleCategory == null) {
            category = EntityFactory.getInstance().createEntity(Category.class);
            category.setName(module.getModuleDescriptor().getDescription());
            category.setDescription(MessageFormat.format(LocaleManager.getString("mainCategoryDescription"), module.getModuleDescriptor().getDescription()));
            category = CategoryDataManager.getInstance().addDatabaseEntity(category);
        } else {
            category = new Category();
            category.updateFrom(moduleCategory);
        }
        if (includeDynamicCategories) {
            category.getChildCategories().addAll(JavaConversions.asJavaList(module.getDynamicCategories()));
        }

        return category;
    }

    public Category getCategoryFromPath(Category root, String... path) {
        Category pathCategory = null;

        if (path.length == 0 || root == null) {
            return root;
        }

        root = CategoryDataManager.getInstance().getCategory(root.id());
        if (root == null) {
            return pathCategory;
        }
        for (Category category : root.getChildCategories()) {
            if (category.getName().equals(path[0])) {
                pathCategory = getCategoryFromPath(category, path.length == 1 ? new String[0] : Arrays.copyOfRange(path, 1, path.length));
                if (pathCategory != null) {
                    return pathCategory;
                }
            }
        }

        return pathCategory;
    }

    public List<CategoryPathWrapper> getCategoryPathWrappers(DefaultMutableTreeNode root) {
        List<CategoryPathWrapper> pathWrappers = new ArrayList<CategoryPathWrapper>();

        fillCategoryPathWrappers(pathWrappers, root);

        return pathWrappers;
    }

    private void fillCategoryPathWrappers(List<CategoryPathWrapper> pathWrappers, DefaultMutableTreeNode node) {
        if (node.getUserObject() instanceof Categorized) {
            pathWrappers.add(new CategoryPathWrapper(getStringPath(node.getPath()), (Categorized) node.getUserObject()));
        }

        for (int index = 0; index < node.getChildCount(); index++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(index);

            fillCategoryPathWrappers(pathWrappers, child);
        }
    }

    private String[] getStringPath(TreeNode[] path) {
        List<String> stringPath = new ArrayList<String>();

        for (TreeNode treeNode : path) {
            if (((DefaultMutableTreeNode) treeNode).getUserObject() instanceof Category) {
                stringPath.add(((Category) ((DefaultMutableTreeNode) treeNode).getUserObject()).getName());
            }
        }

        return stringPath.toArray(new String[0]);
    }

    public TreeNode getCategorizedRoot(List<Categorized> categorizeds, List<CategoryNodeWrapper> categoryNodeWrappers, boolean addUncategorizedNode) {
        DefaultMutableTreeNode root;
        DefaultMutableTreeNode node;
        List<DefaultMutableTreeNode> children;

        root = new DefaultMutableTreeNode("ROOT");

        for (CategoryNodeWrapper categoryNodeWrapper : categoryNodeWrappers) {
            children = Collections.list(root.postorderEnumeration());
            for (DefaultMutableTreeNode child : children) {
                if (!child.isLeaf()) {
                    continue;
                }

                node = createTreeNode(categoryNodeWrapper);
                if (node.isLeaf()) {
                    child.add(node);
                } else {
                    children = Collections.list(node.children());
                    for (DefaultMutableTreeNode subChild : children) {
                        child.add(subChild);
                    }
                }
                if (addUncategorizedNode) {
                    child.add(new DefaultMutableTreeNode(new UnCategory(
                            child.getUserObject() instanceof Category ? (Category) child.getUserObject() : null
                    )));
                }
            }
        }
        fillLeaves(root, categorizeds);
        removeEmptyUncategorized(root);
        removeEmptyNodes(root);

        return root;
    }

    private void removeEmptyNodes(DefaultMutableTreeNode node) {
        for (int index = node.getChildCount() - 1; index >= 0; index--) {
            final DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(index);
            if (!child.isLeaf() && child.getUserObject() instanceof Category) {
                removeEmptyNodes(child);
            }
            if (child.isLeaf() && child.getUserObject() instanceof Category) {
                child.removeFromParent();
            }
        }
    }

    private void removeEmptyUncategorized(DefaultMutableTreeNode root) {
        final Enumeration<DefaultMutableTreeNode> nodes = root.postorderEnumeration();

        while (nodes.hasMoreElements()) {
            final DefaultMutableTreeNode node = nodes.nextElement();
            if (!(node.getUserObject() instanceof UnCategory)) {
                continue;
            }
            if (isEmpty(node)) {
                node.removeFromParent();
            }
        }
    }

    private boolean isEmpty(DefaultMutableTreeNode node) {
        final Enumeration<DefaultMutableTreeNode> nodes = node.postorderEnumeration();

        while (nodes.hasMoreElements()) {
            final DefaultMutableTreeNode descendent = nodes.nextElement();
            if (!(descendent.getUserObject() instanceof Category)) {
                return false;
            }
        }

        return true;
    }


    private DefaultMutableTreeNode createTreeNode(CategoryNodeWrapper wrapper) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(wrapper.getCategory());

        if (wrapper.getDepth() == 0) {
            return node;
        }
        for (Category childCategory : wrapper.getCategory().getChildCategories()) {
            node.add(createTreeNode(new CategoryNodeWrapper(childCategory, wrapper.getDepth() - 1)));
        }

        return node;
    }

    private void fillLeaves(DefaultMutableTreeNode root, List<Categorized> categorizeds) {
        final Enumeration<DefaultMutableTreeNode> nodes = root.postorderEnumeration();

        while (nodes.hasMoreElements()) {
            final DefaultMutableTreeNode node = nodes.nextElement();

            if (!node.isLeaf() || !(node.getUserObject() instanceof Category) || node.getUserObject() instanceof UnCategory) {
                continue;
            }
            for (Categorized categorized : categorizeds) {
                if (accept(node, categorized)) {
                    node.add(new DefaultMutableTreeNode(categorized));
                }
            }
        }
    }

    private boolean accept(DefaultMutableTreeNode node, Categorized categorized) {
        if (!(node.getUserObject() instanceof Category)) {
            return false;
        }

        final Category category = (Category) node.getUserObject();

        if (!categorized.getCategories().contains(category)
                && (!(category instanceof DynamicCategory) || !((DynamicCategory) category).acceptCategorized(categorized))) {
            return false;
        }
        node = (DefaultMutableTreeNode) node.getParent();

        //root
        if (node != null && node.getParent() == null) {
            return true;
        }
        //uncategorized
        if (acceptUncategorized(node, categorized)) {
            return true;
        }

        return accept(node, categorized);
    }

    private boolean acceptUncategorized(DefaultMutableTreeNode node, Categorized categorized) {
        if (!(node.getUserObject() instanceof UnCategory)) {
            return false;
        }

        for (Object object : Collections.list(node.getParent().children())) {
            if (object == node) {
                continue;
            }
            if (accept((DefaultMutableTreeNode) object, categorized)) {
                return false;
            }
        }

        return node.getParent().getParent() == null || accept((DefaultMutableTreeNode) node.getParent(), categorized);
    }

    @Override
    protected String getDefaultQuery() {
        return "from " + Category.class.getName();
    }

    @Override
    protected DataManager getDataManager() {
        return CategoryDataManager.getInstance();
    }

    public void importCategories(List<Category> categories) {
        CategoryDataManager.getInstance().persistDatabaseEntities(categories);
    }

    public static class UnCategory extends Category {
        private UnCategory(Category parentCategory) {
            super(parentCategory, LocaleManager.getString("uncategorized"), UNCATEGORIZED_ID);
        }
    }
}