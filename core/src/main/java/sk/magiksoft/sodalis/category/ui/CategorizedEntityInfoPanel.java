
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.category.ui;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTree;
import sk.magiksoft.sodalis.category.CategoryDataManager;
import sk.magiksoft.sodalis.category.CategoryManager;
import sk.magiksoft.sodalis.category.entity.Categorized;
import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.core.data.DataListener;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.module.Module;
import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.dnd.*;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;

/**
 * @author wladimiiir
 */
public abstract class CategorizedEntityInfoPanel extends AbstractInfoPanel implements DragSourceListener, DropTargetListener, DragGestureListener, DataListener {
    protected static final Comparator<Category> CATEGORY_COMPARATOR = new Comparator<Category>() {
        @Override
        public int compare(Category o1, Category o2) {
            return Collator.getInstance().compare(o1.getName(), o2.getName());
        }
    };
    protected JXTree categoriesTree;
    protected JXList categorizedList;
    protected Categorized categorized;
    protected Component draggedComponent;
    protected boolean includeDynamicCategories;
    protected boolean expand = true;
    private JPanel layoutPanel;

    public CategorizedEntityInfoPanel() {
        this(false);
    }

    public CategorizedEntityInfoPanel(boolean includeDynamicCategories) {
        this.includeDynamicCategories = includeDynamicCategories;
    }

    @Override public void initLayout() {
        super.initLayout();
        reloadCategoryTree();
        CategoryDataManager.getInstance().addDataListener(this);
    }

    @Override public boolean isMultiSaveSupported() {
        return true;
    }

    protected abstract Class<? extends Module> getModuleClass();

    protected void addCategory(Category category) {
        final Enumeration<Category> en = (Enumeration<Category>) ((DefaultListModel) categorizedList.getModel()).elements();

        while (en.hasMoreElements()) {
            Category element = en.nextElement();
            if (element.id().equals(category.id())) {
                return;
            }
        }
        ((DefaultListModel) categorizedList.getModel()).addElement(category);
    }

    private void removeCategory(Category category) {
        Enumeration<Category> en = (Enumeration<Category>) ((DefaultListModel) categorizedList.getModel()).elements();

        while (en.hasMoreElements()) {
            Category element = en.nextElement();
            if (element.id().equals(category.id())) {
                ((DefaultListModel) categorizedList.getModel()).removeElement(element);
            }
        }
    }


    @Override
    protected Component createLayout() {
        layoutPanel = new JPanel(new GridBagLayout());
        JScrollPane leftScrollPane = new JScrollPane(categoriesTree = new JXTree());
        JScrollPane rightScrollPane = new JScrollPane(categorizedList = new JXList(new DefaultListModel()));
        GridBagConstraints c = new GridBagConstraints();

        categoriesTree.setRootVisible(false);

        DragSource source = new DragSource();
        source.createDefaultDragGestureRecognizer(categoriesTree, DnDConstants.ACTION_COPY_OR_MOVE, this);
        source.addDragSourceListener(this);
        source = new DragSource();
        source.createDefaultDragGestureRecognizer(categorizedList, DnDConstants.ACTION_COPY_OR_MOVE, this);
        source.addDragSourceListener(this);
        categoriesTree.setDragEnabled(true);
        categoriesTree.setDropTarget(new DropTarget(categoriesTree, this));
        categorizedList.setDragEnabled(true);
        categorizedList.setDropTarget(new DropTarget(categorizedList, this));

        categorizedList.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof Category && component instanceof JLabel) {
                    StringBuilder path = new StringBuilder();
                    Category category = (Category) value;

                    while (category.getParentCategory() != null) {
                        if (path.length() > 0) {
                            path.insert(0, " \u2192 ");
                        }
                        path.insert(0, category.getName());
                        category = category.getParentCategory();
                    }

                    ((JLabel) component).setText(path.toString());
                }

                return component;
            }
        });

        categorizedList.getModel().addListDataListener(listDataListener);

        c.gridx = c.gridy = 0;
        c.insets = new Insets(3, 3, 1, 0);
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.WEST;
        layoutPanel.add(new JLabel(LocaleManager.getString("available") + ":"), c);

        c.gridx++;
        c.insets = new Insets(3, 3, 1, 3);
        layoutPanel.add(new JLabel(LocaleManager.getString("choosen") + ":"), c);

        c.gridx = 0;
        c.gridy++;
        c.insets = new Insets(0, 3, 3, 0);
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        layoutPanel.add(leftScrollPane, c);

        c.gridx++;
        c.insets = new Insets(0, 3, 3, 3);
        layoutPanel.add(rightScrollPane, c);

        layoutPanel.setPreferredSize(new Dimension(300, 200));

        return layoutPanel;
    }

    @Override
    public String getPanelName() {
        return LocaleManager.getString("categories");
    }

    @Override
    public void setupObject(Object object) {
        if (!(object instanceof Categorized)) {
            return;
        }

        final Categorized categorizedObject = (Categorized) object;
        categorizedObject.getCategories().clear();
        for (int index = 0; index < ((DefaultListModel) categorizedList.getModel()).size(); index++) {
            categorizedObject.getCategories().add((Category) ((DefaultListModel) categorizedList.getModel()).get(index));
        }
    }

    @Override
    public void setupPanel(Object object) {
        if (!(object instanceof Categorized)) {
            return;
        }

        categorized = (Categorized) object;
        initialized = false;
    }

    @Override
    public void initData() {
        if (initialized || categorized == null) {
            return;
        }

        ((DefaultListModel) categorizedList.getModel()).clear();
        for (Category category : categorized.getCategories()) {
            addCategory(category);
        }

        initialized = true;
    }

    public void reloadCategoryTree() {
        final Category rootCategory = CategoryManager.getInstance().getRootCategory(getModuleClass(), includeDynamicCategories);
        if(rootCategory==null){
            return;
        }
        Collections.sort(rootCategory.getChildCategories(), CATEGORY_COMPARATOR);
        categoriesTree.setModel(new DefaultTreeModel(new CategoryTreeNode(rootCategory)));
        if(expand){
            categoriesTree.expandAll();
        }
    }

    @Override
    public void entitiesAdded(List<? extends DatabaseEntity> entities) {
        for (Object object : entities) {
            if (object instanceof Category) {
                reloadCategoryTree();
                return;
            }
        }
    }

    @Override
    public void entitiesRemoved(List<? extends DatabaseEntity> entities) {
        for (Object object : entities) {
            if (object instanceof Category) {
                reloadCategoryTree();
                return;
            }
        }
    }

    @Override
    public void entitiesUpdated(List<? extends DatabaseEntity> entities) {
        for (Object object : entities) {
            if (object instanceof Category) {
                reloadCategoryTree();
                return;
            }
        }
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        if (dtde.getDropTargetContext().getComponent() == draggedComponent) {
            draggedComponent = null;
            return;
        }

        if (draggedComponent == categoriesTree) {
            addCategory(((CategoryTreeNode) categoriesTree.getSelectionPath().getLastPathComponent()).getDatabaseEntity());
        } else if (draggedComponent == categorizedList) {
            removeCategory((Category) categorizedList.getSelectedValue());
        }

        draggedComponent = null;
    }

    protected boolean acceptDrag() {
        return ((CategoryTreeNode) categoriesTree.getSelectionPath().getLastPathComponent()).isLeaf();
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        if (dtde.getDropTargetContext().getComponent() == draggedComponent
                || (draggedComponent == categoriesTree && !acceptDrag())) {
            dtde.rejectDrag();
        }
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    @Override
    public void dragGestureRecognized(DragGestureEvent dge) {
        draggedComponent = dge.getComponent();
    }

    @Override
    public void dragDropEnd(DragSourceDropEvent dsde) {
    }

    @Override
    public void dragEnter(DragSourceDragEvent dsde) {
    }

    @Override
    public void dragExit(DragSourceEvent dse) {
    }

    @Override
    public void dragOver(DragSourceDragEvent dsde) {
    }

    @Override
    public void dropActionChanged(DragSourceDragEvent dsde) {
    }
}