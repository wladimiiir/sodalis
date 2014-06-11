
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

import sk.magiksoft.sodalis.category.CategoryManager;
import sk.magiksoft.sodalis.category.entity.Categorized;
import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.category.entity.CategoryNodeWrapper;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.data.DataListener;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.factory.IconFactory;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.module.Module;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.sodalis.core.ui.OkCancelDialog;
import sk.magiksoft.swing.ISTreeTable;
import sk.magiksoft.swing.table.SelectionListener;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author wladimiiir
 */
public class CategoryTreeComponent implements DataListener {
    private EventListenerList listenerList = new EventListenerList();
    private JScrollPane parentScrollPane;
    private Class<? extends Module> moduleClass;
    private Component oldScrollPaneComponent;
    private JButton showCategoryTreeButton;
    private ISTreeTable treeTable;
    private ObjectTableModel objectTableModel;
    private CategoryTreeTableModelWrapper treeTableModelWrapper;
    private ChooseCategoriesPanel chooseCategoriesPanel;

    public CategoryTreeComponent(Class<? extends Module> moduleClass, ObjectTableModel objectTableModel, JScrollPane parentScrollPane) {
        this.moduleClass = moduleClass;
        this.objectTableModel = objectTableModel;
        this.parentScrollPane = parentScrollPane;
        initComponents();
    }

    public AbstractButton getShowCategoryTreeButton() {
        return showCategoryTreeButton;
    }

    private void initComponents() {
        chooseCategoriesPanel = new ChooseCategoriesPanel(moduleClass);
        showCategoryTreeButton = new JButton(new ChooseCategoriesTreeAction());
        treeTableModelWrapper = new CategoryTreeTableModelWrapper(objectTableModel);

        treeTable = new ISTreeTable(treeTableModelWrapper);
        treeTable.setRowSorter(new TableRowSorter<TableModel>(objectTableModel));
        treeTable.setLeafIcon(null);
    }

    public void addSelectionListener(SelectionListener listener) {
        treeTable.addSelectionListener(listener);
    }

    public void addTreeSelectionListener(TreeSelectionListener listener) {
        treeTable.addTreeSelectionListener(listener);
    }

    public void refresh(boolean componentVisible, List<Category> selectedCategories) {
        if (componentVisible != isComponentShown()) {
            swapScrollPaneViewportComponents();
        }

        chooseCategoriesPanel.setSelectedCategories(selectedCategories);
        refresh();
    }

    private void swapScrollPaneViewportComponents() {
        if (oldScrollPaneComponent == null) {
            oldScrollPaneComponent = parentScrollPane.getViewport().getView();
            parentScrollPane.setViewportView(treeTable);
        } else {
            parentScrollPane.setViewportView(oldScrollPaneComponent);
            oldScrollPaneComponent = null;
        }
    }

    private void setRoot(TreeNode root) {
        DefaultMutableTreeNode modelRoot = (DefaultMutableTreeNode) treeTableModelWrapper.getRoot();

        modelRoot.removeAllChildren();
        final List<MutableTreeNode> children = Collections.list(root.children());
        for (Object object : children) {
            modelRoot.add((MutableTreeNode) object);
        }
        treeTableModelWrapper.fireTreeTableModelChanged(new TreePath(modelRoot));
    }

    public boolean isComponentShown() {
        return oldScrollPaneComponent != null;
    }

    public DefaultMutableTreeNode getRoot() {
        return (DefaultMutableTreeNode) treeTableModelWrapper.getRoot();
    }

    public String[] getSelectedCategoryPath() {
        String[] path = new String[chooseCategoriesPanel.getSelectedCategories().size()];

        for (int i = 0; i < chooseCategoriesPanel.getSelectedCategories().size(); i++) {
            Category category = chooseCategoriesPanel.getSelectedCategories().get(i);
            path[i] = category.getName();
        }

        return path;
    }

    public List<Category> getSelectedCategories() {
        return chooseCategoriesPanel.getSelectedCategories();
    }

    public List getSelectedObjects() {
        List result = new ArrayList();

        for (int i = 0; i < treeTable.getSelectedRows().length; i++) {
            int row = treeTable.getSelectedRows()[i];
            Object object = treeTable.getPathForRow(row);
            if (object instanceof TreePath) {
                object = ((TreePath) object).getLastPathComponent();
            }
            if (!(object instanceof DefaultMutableTreeNode) || !(((DefaultMutableTreeNode) object).getUserObject() instanceof Categorized)) {
                continue;
            }
            result.add(((DefaultMutableTreeNode) object).getUserObject());
        }

        return result;
    }

    @Override
    public void entitiesAdded(List<? extends DatabaseEntity> entities) {
        if (!isComponentShown()) {
            return;
        }

        for (Object object : entities) {
        }
    }

    @Override
    public void entitiesUpdated(List<? extends DatabaseEntity> entities) {
        if (!isComponentShown()) {
            return;
        }
    }

    @Override
    public void entitiesRemoved(List<? extends DatabaseEntity> entities) {
        if (!isComponentShown()) {
            return;
        }

    }

    public void refresh() {
        if (!isComponentShown()) {
            return;
        }

        List<CategoryNodeWrapper> wrappers = getCategoryNodeWrappers(chooseCategoriesPanel.getSelectedCategories());
        TreeNode root = CategoryManager.getInstance().getCategorizedRoot(objectTableModel.getObjects(), wrappers, chooseCategoriesPanel.isShowUncategorized());

        setRoot(root);
    }

    public void repaint() {
        if (!isComponentShown()) {
            return;
        }
        treeTable.repaint();
    }

    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    private void fireActionPerformed(ActionEvent e) {
        ActionListener[] listeners = listenerList.getListeners(ActionListener.class);

        for (ActionListener listener : listeners) {
            listener.actionPerformed(e);
        }
    }

    private class ChooseCategoriesTreeAction extends AbstractAction {

        private OkCancelDialog chooseCategoriesDialog;

        public ChooseCategoriesTreeAction() {
            super(null, IconFactory.getInstance().getIcon("categoryTree"));
            putValue(Action.SHORT_DESCRIPTION, LocaleManager.getString("structureViewByCategory"));
        }

        private void initComponents() {
            chooseCategoriesPanel.reloadCategoryTree();

            if (chooseCategoriesDialog != null) {
                return;
            }

            chooseCategoriesDialog = new OkCancelDialog(SodalisApplication.get().getMainFrame(), LocaleManager.getString("chooseCategories"));
            chooseCategoriesDialog.setModal(true);
            chooseCategoriesDialog.setMainPanel(chooseCategoriesPanel);
            chooseCategoriesDialog.setSize(400, 300);
            chooseCategoriesDialog.setLocationRelativeTo(null);
            chooseCategoriesDialog.getOkButton().addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    List<CategoryNodeWrapper> wrappers = getCategoryNodeWrappers(chooseCategoriesPanel.getSelectedCategories());
                    TreeNode root = CategoryManager.getInstance().getCategorizedRoot(objectTableModel.getObjects(), wrappers, chooseCategoriesPanel.isShowUncategorized());

                    setRoot(root);
                    swapScrollPaneViewportComponents();
                    refreshAction();
                    fireActionPerformed(e);
                }
            });
        }

        private void refreshAction() {
            final Icon icon = isComponentShown()
                    ? IconFactory.getInstance().getIcon("table")
                    : IconFactory.getInstance().getIcon("categoryTree");
            final String tooltip = isComponentShown()
                    ? LocaleManager.getString("normalView")
                    : LocaleManager.getString("structureViewByCategory");

            putValue(AbstractAction.SMALL_ICON, icon);
            putValue(Action.SHORT_DESCRIPTION, tooltip);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isComponentShown()) {
                initComponents();
                chooseCategoriesDialog.setVisible(true);
            } else {
                swapScrollPaneViewportComponents();
                refreshAction();
                fireActionPerformed(e);
            }
        }
    }

    private List<CategoryNodeWrapper> getCategoryNodeWrappers(List<Category> categories) {
        List<CategoryNodeWrapper> wrappers = new ArrayList<CategoryNodeWrapper>();
        int depth = 10;

        for (Category category : categories) {
            wrappers.add(new CategoryNodeWrapper(category, depth));
        }

        return wrappers;
    }
}