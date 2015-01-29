package sk.magiksoft.sodalis.core.ui;

import sk.magiksoft.sodalis.category.ui.CategoryTreeComponent;
import sk.magiksoft.sodalis.core.action.ActionMessage;
import sk.magiksoft.sodalis.core.action.MessageAction;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntityContainer;
import sk.magiksoft.sodalis.core.entity.Entity;
import sk.magiksoft.sodalis.core.function.ResultFunction;
import sk.magiksoft.sodalis.core.injector.Injector;
import sk.magiksoft.sodalis.core.module.Module;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.sodalis.core.utils.CollectionUtils;
import sk.magiksoft.swing.ISTable;
import sk.magiksoft.swing.table.SelectionListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wladimiiir
 */
public abstract class AbstractTableContext extends AbstractContext {
    protected JPopupMenu popupMenu;
    protected ISTable table;
    protected SelectionListener selectionListener = this::canChangeEntity;
    protected CategoryTreeComponent categoryTreeComponent;
    private List<MessageAction> messageActions = new LinkedList<MessageAction>();

    public AbstractTableContext(Class<? extends DatabaseEntity> contextClass, ISTable table) {
        super(contextClass);
        this.table = table;
        Injector.inject(this);
        initTable();
    }

    public ISTable getTable() {
        return table;
    }

    protected void initTable() {
        table.setBorder(BorderFactory.createEmptyBorder());
        table.addSelectionListener(selectionListener);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting() || adjusting) {
                return;
            }

            currentObject = loadObject(getSelectedObject());
            currentObjectChanged();
            reloadControlPanel();
        });
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    return;
                }
                preparePopupMenu(getSelectedEntities());
                if (popupMenu.getComponentCount() == 0) {
                    return;
                }
                popupMenu.show(table, e.getX(), e.getY());
            }
        });
    }

    protected void registerMessageAction(MessageAction action) {
        messageActions.add(action);
    }

    protected void preparePopupMenu(List<? extends Entity> entities) {
        if (popupMenu == null) {
            popupMenu = new JPopupMenu();
        } else {
            popupMenu.removeAll();
        }
    }

    protected ObjectTableModel getModel(Class modelObjectClass) {
        return (ObjectTableModel) table.getModel();
    }

    @Override
    protected void currentObjectChanged() {
        for (MessageAction action : messageActions) {
            final ActionMessage actionMessage = action.getActionMessage(getSelectedEntities());
            action.setEnabled(actionMessage.isAllowed());
            action.putValue(Action.SHORT_DESCRIPTION, actionMessage.getMessage());
        }
    }

    @Override
    public void entitiesAdded(List<? extends DatabaseEntity> entities) {
        List<Long> entityIDs = null;
        for (Object object : entities) {
            if (!(object instanceof DatabaseEntity) || ((DatabaseEntity) object).isDeleted() || !acceptEntity((DatabaseEntity) object, false)) {
                continue;
            }
            final DatabaseEntity entity = (DatabaseEntity) object;

            if (entityIDs == null) {
                entityIDs = new LinkedList<Long>();
                for (Object modelObject : getModel(entity.getClass()).getObjects()) {
                    if (!(modelObject instanceof DatabaseEntity)) {
                        continue;
                    }
                    entityIDs.add(((DatabaseEntity) modelObject).getId());
                }
            }

            if (!entityIDs.contains(entity.getId())) {
                getModel(entity.getClass()).addObject(entity);
                entityIDs.add(entity.getId());
                if (categoryTreeComponent != null && categoryTreeComponent.isComponentShown()) {
                    categoryTreeComponent.refresh();
                }
            }
        }
        if (controlPanel != null) {
            controlPanel.entitiesAdded(entities);
        }
    }

    @Override
    public void entitiesUpdated(List<? extends DatabaseEntity> entities) {
        List<DatabaseEntity> toRemove = new LinkedList<DatabaseEntity>();
        boolean found = false;

        for (DatabaseEntity entity : entities) {
            if (!acceptEntity(entity, true)) {
                continue;
            }
            for (int i = 0; i < getModel(entity.getClass()).getRowCount(); i++) {
                DatabaseEntity modelEntity = (DatabaseEntity) getModel(entity.getClass()).getObject(i);
                if (modelEntity.getId().equals(entity.getId())) {
                    found = true;
                    if (entity.isDeleted()) {
                        toRemove.add(entity);
                    } else if (modelEntity != entity) {
                        modelEntity.updateFrom(entity);
                    }
                    break;
                } else if (modelEntity instanceof DatabaseEntityContainer) {
                    for (DatabaseEntity databaseEntity : ((DatabaseEntityContainer) modelEntity).getDatabaseEntities(entity.getClass())) {
                        if (databaseEntity.getId().equals(entity.getId())) {
                            databaseEntity.updateFrom(entity);
                        }
                    }
                }
            }
        }
        if (found) {
            if (toRemove.size() > 0) {
                entitiesRemoved(toRemove);
            } else {
                if (categoryTreeComponent != null && categoryTreeComponent.isComponentShown()) {
                    categoryTreeComponent.refresh();
                } else {
                    fireTableDataChanged();
                }
            }
        }

        if (controlPanel != null) {
            controlPanel.entitiesUpdated(entities);
        }
    }

    @Override
    public void entitiesRemoved(List<? extends DatabaseEntity> entities) {
        boolean found = false;
        for (Object object : entities) {
            if (!(object instanceof DatabaseEntity)) {
                continue;
            }

            DatabaseEntity entity = (DatabaseEntity) object;
            for (int i = 0; i < getModel(entity.getClass()).getObjects().size(); i++) {
                DatabaseEntity modelEntity = (DatabaseEntity) getModel(entity.getClass()).getObjects().get(i);
                if (modelEntity.getId().equals(entity.getId())) {
                    getModel(entity.getClass()).removeObject(modelEntity);
                    found = true;
                    break;
                }
            }
        }
        if (found) {
            if (categoryTreeComponent != null && categoryTreeComponent.isComponentShown()) {
                categoryTreeComponent.refresh();
            } else {
                fireTableDataChanged();
            }
        }
        if (controlPanel != null) {
            controlPanel.entitiesRemoved(entities);
        }
    }

    protected void fireTableDataChanged() {
        int[] selected = table.getSelectedRows();
        getModel(null).fireTableDataChanged();
        for (int i = 0; i < selected.length; i++) {
            int sel = selected[i];
            if (getModel(null).getRowCount() > sel) {
                table.getSelectionModel().addSelectionInterval(sel, sel);
            }
        }
    }

    public void removeAllRecords() {
        getModel(Object.class).removeAllObjects();
    }

    public Object getSelectedObject() {
        if (categoryTreeComponent == null || !categoryTreeComponent.isComponentShown()) {
            if (table.getSelectedRow() < 0) {
                return null;
            }
            return getModel(null).getObject(table.getSelectedRow());
        } else {
            List selected = categoryTreeComponent.getSelectedObjects();

            return selected.isEmpty() ? null : selected.get(0);
        }
    }

    public void addTableSelectionListener(ListSelectionListener listener) {
        table.getSelectionModel().addListSelectionListener(listener);
    }

    public void removeTableSelectionListener(ListSelectionListener listener) {
        table.getSelectionModel().removeListSelectionListener(listener);
    }

    public CategoryTreeComponent getCategoryTreeComponent() {
        return categoryTreeComponent;
    }

    @Override
    public List<? extends Entity> getSelectedEntities() {
        ObjectTableModel model;
        List result;

        if (categoryTreeComponent == null || !categoryTreeComponent.isComponentShown()) {
            result = new ArrayList();
            if (!(table.getModel() instanceof ObjectTableModel)) {
                return result;
            }
            model = (ObjectTableModel) table.getModel();
            for (int i = 0; i < table.getSelectedRows().length; i++) {
                int row = table.getSelectedRows()[i];
                if (row < 0 || row >= model.getRowCount()) {
                    continue;
                }
                Object object = model.getObject(row);
                if (object == null) {
                    continue;
                }
                result.add(object);
            }
        } else {
            result = categoryTreeComponent.getSelectedObjects();
        }

        return result;
    }

    @Override
    public void setSelectedEntities(List<? extends Entity> entities) {
        final ListSelectionModel selectionModel = table.getSelectionModel();
        Entity selected = null;

        adjusting = true;
        selectionModel.clearSelection();
        adjusting = false;
        if (!(table.getModel() instanceof ObjectTableModel)) {
            return;
        }
        final ObjectTableModel model = (ObjectTableModel) table.getModel();
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
                    adjusting = true;
                    selectionModel.addSelectionInterval(i, i);
                    if (selected == null) {
                        selected = entity;
                    }
                    adjusting = false;
                }
            }
        }
        currentObject = selected == null ? null : loadObject(selected);
        currentObjectChanged();
        reloadControlPanel();
    }

    @Override
    public List<? extends Entity> getEntities() {
        return new ArrayList<>(((ObjectTableModel) table.getModel()).getObjects());
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
        if (result != ISOptionPane.CANCEL_OPTION) {
            SwingUtilities.invokeLater(() -> table.getSelectionModel().setValueIsAdjusting(false));
        }
        return result != ISOptionPane.CANCEL_OPTION;
    }

    protected ObjectTableModel getCategoryTreeComponentTableModel() {
        return getModel(null);
    }

    protected void initCategoryTreeComponent(final Class<? extends Module> moduleClass, final JScrollPane scrollPane) {
        categoryTreeComponent = new CategoryTreeComponent(moduleClass, getCategoryTreeComponentTableModel(), scrollPane);
        categoryTreeComponent.addSelectionListener(selectionListener);
        categoryTreeComponent.addTreeSelectionListener(e -> {
            currentObject = categoryTreeComponent.getSelectedObjects().isEmpty() ? null : categoryTreeComponent.getSelectedObjects().get(0);
            currentObjectChanged();
            reloadControlPanel();
        });
    }

    protected void reloadControlPanel() {
        if (controlPanel == null) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            controlPanel.setupControlPanel(currentObject);
            controlPanel.setAdditionalObjects(CollectionUtils.filter((List) getSelectedEntities(), new ResultFunction<Boolean, Object>() {
                @Override
                public Boolean apply(Object object) {
                    return currentObject != object;
                }
            }));
        });
    }
}
