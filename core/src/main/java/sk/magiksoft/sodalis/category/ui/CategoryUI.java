
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

import sk.magiksoft.sodalis.category.action.AddCategoryAction;
import sk.magiksoft.sodalis.category.action.CategoryExportAction;
import sk.magiksoft.sodalis.category.action.CategoryImportAction;
import sk.magiksoft.sodalis.category.action.RemoveCategoryAction;
import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.action.ActionMessage;
import sk.magiksoft.sodalis.core.action.MessageAction;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.treetable.DatabaseEntityTreeNode;
import sk.magiksoft.sodalis.core.ui.AbstractTreeTableContext;
import sk.magiksoft.sodalis.core.utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.List;

/**
 * @author wladimiiir
 */
public class CategoryUI extends AbstractTreeTableContext {

    private JButton btnAdd;
    private JButton btnRemove;
    private JButton btnExport;
    private JButton btnImport;

    public CategoryUI() {
        super(Category.class, new CategoryTreeTableModel(), new CategoryControlPanel());
        initComponents();
        SodalisApplication.get().getStorageManager().registerComponent("categoryUI", this);
    }

    private void initComponents() {
        JToolBar toolBar = UIUtils.createToolBar();

        toolBar.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        btnAdd = new JButton(new AddCategoryAction());
        btnRemove = new JButton(new RemoveCategoryAction());
        btnExport = new JButton(new CategoryExportAction());
        btnImport = new JButton(new CategoryImportAction());

        initToolbarButton(btnAdd);
        initToolbarButton(btnRemove);
        initToolbarButton(btnExport);
        initToolbarButton(btnImport);

        toolBar.add(btnAdd);
        toolBar.add(btnRemove);
        toolBar.add(btnExport);
        toolBar.add(btnImport);

        add(toolBar, BorderLayout.NORTH);
    }

    private void setupButtons() {
        final List selectedObjects = getSelectedEntities();
        ActionMessage message = ((MessageAction) btnAdd.getAction()).getActionMessage(selectedObjects);

        btnAdd.setToolTipText(message.getMessage());
        btnAdd.setEnabled(message.isAllowed());

        message = ((MessageAction) btnRemove.getAction()).getActionMessage(selectedObjects);
        btnRemove.setToolTipText(message.getMessage());
        btnRemove.setEnabled(message.isAllowed());

        message = ((MessageAction) btnExport.getAction()).getActionMessage(selectedObjects);
        btnExport.setToolTipText(message.getMessage());
        btnExport.setEnabled(message.isAllowed());

        message = ((MessageAction) btnImport.getAction()).getActionMessage(selectedObjects);
        btnImport.setToolTipText(message.getMessage());
        btnImport.setEnabled(message.isAllowed());
    }

    @Override
    protected void currentObjectChanged() {
        setupButtons();
    }

    @Override
    protected boolean acceptEntity(Object entity) {
        return entity instanceof Category && super.acceptEntity(entity);
    }

    @Override
    protected DatabaseEntityTreeNode findParentNode(DatabaseEntityTreeNode node, DatabaseEntity entity) {
        Enumeration<DatabaseEntityTreeNode> children = node.children();
        Category category = ((Category) entity).getParentCategory();

        if (category == null) {
            return (DatabaseEntityTreeNode) treeTableModel.getRoot();
        }
        if (node.getDatabaseEntity().getId() != null && node.getDatabaseEntity().getId().equals(category.getId())) {
            return node;
        }

        while (children.hasMoreElements()) {
            node = children.nextElement();
            node = findParentNode(node, entity);
            if (node != null) {
                return node;
            }
        }

        return null;
    }

    @Override
    protected String getLeftSplitText() {
        return LocaleManager.getString("categoryList");
    }

    @Override
    protected String getRightSplitText() {
        return LocaleManager.getString("details");
    }
}