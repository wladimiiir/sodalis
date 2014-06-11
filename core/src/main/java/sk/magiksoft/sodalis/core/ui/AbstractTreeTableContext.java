
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.ui;

import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.entity.Entity;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.sodalis.core.treetable.DatabaseEntityTreeNode;
import sk.magiksoft.sodalis.core.treetable.ObjectTreeTableModel;
import sk.magiksoft.sodalis.core.ui.controlpanel.ControlPanel;
import sk.magiksoft.swing.HideableSplitPane;
import sk.magiksoft.swing.ISTreeTable;
import sk.magiksoft.swing.table.SelectionListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * @author wladimiiir
 */
public abstract class AbstractTreeTableContext extends AbstractContext {

    protected ISTreeTable treeTable;
    protected ObjectTreeTableModel treeTableModel;

    public AbstractTreeTableContext(Class<? extends DatabaseEntity> contextClass, ObjectTreeTableModel treeTableModel, ControlPanel controlPanel) {
        super(contextClass);
        this.treeTableModel = treeTableModel;
        this.treeTable = new ISTreeTable(treeTableModel);
        this.controlPanel = controlPanel;
        initTreeTable();
        initComponents();
    }

    protected void initTreeTable() {
        treeTable.setBorder(BorderFactory.createEmptyBorder());
        treeTable.addSelectionListener(new SelectionListener() {

            @Override
            public boolean selectionWillBeChanged() {
                return canChangeEntity();
            }
        });
        treeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(e.getValueIsAdjusting()){
                    return;
                }
                currentObject = loadObject(getSelectedObject());
                currentObjectChanged();
                reloadControlPanel();
            }
        });
    }

    protected boolean acceptEntity(Object entity) {
        return entity instanceof DatabaseEntity;
    }

    @Override
    public void entitiesAdded(List<? extends DatabaseEntity> entities) {
        records:
        for (Object object : entities) {
            if (!acceptEntity(object) || ((DatabaseEntity) object).isDeleted()) {
                continue;
            }
            DatabaseEntity entity = (DatabaseEntity) object;
            DatabaseEntityTreeNode node = findParentNode((DatabaseEntityTreeNode) treeTable.getTreeTableModel().getRoot(), entity);

            if (node == null) {
                continue;
            }
            Enumeration<DatabaseEntityTreeNode> children = node.children();
            while (children.hasMoreElements()) {
                if (children.nextElement().getDatabaseEntity().getId().equals(entity.getId())) {
                    continue records;
                }
            }
            node.addDatabaseEntity(entity);
            fireTableDataChanged(node.getTreePath());
        }
        if (!entities.isEmpty()) {
            setSelectedObject((DatabaseEntity) entities.get(0));
        }
    }

    @Override
    public void entitiesUpdated(List<? extends DatabaseEntity> entities) {
        for (Object object : entities) {
            if (!acceptEntity(object)) {
                continue;
            }

            DatabaseEntity entity = (DatabaseEntity) object;
            DatabaseEntityTreeNode node = findEntityNode((DatabaseEntityTreeNode) treeTable.getTreeTableModel().getRoot(), entity);
            if (node == null) {
                continue;
            }
            node.getDatabaseEntity().updateFrom(entity);
            fireTableDataChanged(node.getTreePath());
        }
    }

    @Override
    public void entitiesRemoved(List<? extends DatabaseEntity> entities) {
        DatabaseEntityTreeNode node;
        for (Object object : entities) {
            if (!acceptEntity(object)) {
                continue;
            }
            DatabaseEntity entity = (DatabaseEntity) object;
            DatabaseEntityTreeNode parentNode = findParentNode((DatabaseEntityTreeNode) treeTable.getTreeTableModel().getRoot(), entity);
            node = parentNode.removeDatabaseEntity(entity);
            if (node != null) {
                fireTableDataChanged(parentNode.getTreePath());
            }
        }
    }

    protected abstract DatabaseEntityTreeNode findParentNode(DatabaseEntityTreeNode node, DatabaseEntity entity);

    private DatabaseEntityTreeNode findEntityNode(DatabaseEntityTreeNode node, DatabaseEntity entity) {
        Enumeration<DatabaseEntityTreeNode> children = node.children();

        if (node.getDatabaseEntity() != null && node.getDatabaseEntity().getId() != null && node.getDatabaseEntity().getId().equals(entity.getId())) {
            return node;
        }

        while (children.hasMoreElements()) {
            node = children.nextElement();
            node = findEntityNode(node, entity);
            if (node != null) {
                return node;
            }
        }

        return null;
    }

    protected void fireTableDataChanged(TreePath treePath) {
        treeTableModel.fireTreeTableModelChanged(treePath);
    }

    public void removeAllRecords() {
    }

    public Object getSelectedObject() {
        Object selected = treeTable.getPathForRow(treeTable.getSelectedRow());

        if (selected instanceof TreePath) {
            selected = ((DatabaseEntityTreeNode) ((TreePath) selected).getLastPathComponent()).getDatabaseEntity();
        }

        return selected;
    }

    public void addTableSelectionListener(ListSelectionListener listener) {
        treeTable.getSelectionModel().addListSelectionListener(listener);
    }

    public void removeTableSelectionListener(ListSelectionListener listener) {
        treeTable.getSelectionModel().removeListSelectionListener(listener);
    }

    protected void setSelectedObject(DatabaseEntity entity) {
        if (entity == null) {
            treeTable.getSelectionModel().clearSelection();
            return;
        }
    }

    @Override
    public List<? extends Entity> getSelectedEntities() {
        List result = new ArrayList();

        for (int i = 0; i < treeTable.getSelectedRows().length; i++) {
            int row = treeTable.getSelectedRows()[i];
            Object object = treeTable.getPathForRow(row);

            if (object instanceof TreePath) {
                object = ((TreePath) object).getLastPathComponent();
            }
            if (object instanceof DatabaseEntityTreeNode) {
                object = ((DatabaseEntityTreeNode) object).getDatabaseEntity();
            }
            if (object == null) {
                continue;
            }
            result.add(object);
        }

        return result;
    }

    @Override
    public void setSelectedEntities(List<? extends Entity> entities) {
        ObjectTableModel model;
        ListSelectionModel selectionModel = treeTable.getSelectionModel();

        selectionModel.clearSelection();
        if (!(treeTable.getModel() instanceof ObjectTableModel)) {
            return;
        }
        model = (ObjectTableModel) treeTable.getModel();
        for (int i = 0; i < model.getObjects().size(); i++) {
            if (!(model.getObjects().get(i) instanceof DatabaseEntity)) {
                continue;
            }
            DatabaseEntity modelEntity = (DatabaseEntity) model.getObjects().get(i);
            for (Object object : entities) {
                if (!(object instanceof DatabaseEntity)) {
                    continue;
                }
                DatabaseEntity entity = (DatabaseEntity) object;
                if (entity.getId().equals(modelEntity.getId())) {
                    selectionModel.addSelectionInterval(i, i);
                }
            }
        }
    }

    @Override
    public List<? extends Entity> getEntities() {
        return Collections.singletonList((Entity) treeTable.getTreeTableModel().getRoot());
    }

    @Override
    public boolean canChangeEntity() {
        if (controlPanel == null || !controlPanel.isEditing()) {
            return true;
        }
        int result = ISOptionPane.showConfirmDialog(this,
                sk.magiksoft.sodalis.core.locale.LocaleManager.getString("saveChangesQuestion"),
                currentObject == null ? "" : currentObject.toString(), ISOptionPane.YES_NO_CANCEL_OPTION);
        if (result == ISOptionPane.YES_OPTION) {
            controlPanel.doUpdate();
        } else if (result == ISOptionPane.NO_OPTION) {
            controlPanel.cancelEditing();
        }
        return result != ISOptionPane.CANCEL_OPTION;
    }

    protected String getLeftSplitText() {
        return "";
    }

    protected String getRightSplitText() {
        return "";
    }

    private void initComponents() {
        JScrollPane tableScrollPane = new JScrollPane(treeTable);
        JPanel controlPanelContainer = new JPanel(new BorderLayout());
        HideableSplitPane splitPane = new HideableSplitPane(JSplitPane.VERTICAL_SPLIT, tableScrollPane, controlPanelContainer);

        splitPane.setName("splitPane");
        splitPane.setDividerLocation(400);
        splitPane.setLeftText(getLeftSplitText());
        splitPane.setRightText(getRightSplitText());
        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);

        if (controlPanel != null) {
            controlPanelContainer.add(controlPanel.getControlComponent(), BorderLayout.CENTER);
            controlPanel.getControlComponent().setMinimumSize(new Dimension(200, 340));
        }

        treeTable.setAutoCreateRowSorter(true);
    }

    protected void reloadControlPanel() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                controlPanel.setupControlPanel(currentObject);
            }
        });
    }
}