package sk.magiksoft.sodalis.core.context;

import sk.magiksoft.sodalis.core.table.ObjectTableModel;

import java.util.Comparator;
import java.util.List;
import java.util.Vector;

/**
 * @author wladimiiir
 */
public class ObjectTableModelWrapper extends ObjectTableModel {

    private ObjectTableModel objectTableModel;

    public ObjectTableModelWrapper() {
        super(new Object[0]);
    }

    public void setModel(ObjectTableModel objectTableModel) {
        this.objectTableModel = objectTableModel;
    }

    @Override
    public Vector getObjects() {
        return objectTableModel == null ? super.getObjects() : objectTableModel.getObjects();
    }

    @Override
    public Object getObject(int row) {
        return objectTableModel == null ? super.getObject(row) : objectTableModel.getObject(row);
    }

    @Override
    public void setObjects(List objects) {
        if (objectTableModel == null) {
            super.setObjects(objects);
        } else {
            objectTableModel.setObjects(objects);
        }
    }

    @Override
    public void setObjects(List objects, boolean silent) {
        if (objectTableModel == null) {
            super.setObjects(objects, silent);
        } else {
            objectTableModel.setObjects(objects, silent);
        }
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        return objectTableModel == null ? super.getColumnClass(columnIndex) : objectTableModel.getColumnClass(columnIndex);
    }

    @Override
    public int getColumnCount() {
        return objectTableModel == null ? super.getColumnCount() : objectTableModel.getColumnCount();
    }

    @Override
    public Object getColumnIdentificator(int column) {
        return objectTableModel == null ? super.getColumnIdentificator(column) : objectTableModel.getColumnIdentificator(column);
    }

    @Override
    public String getColumnName(int column) {
        return objectTableModel == null ? super.getColumnName(column) : objectTableModel.getColumnName(column);
    }

    @Override
    public Comparator getComparator(int column) {
        return objectTableModel == null ? super.getComparator(column) : objectTableModel.getComparator(column);
    }

    @Override
    public int getRowCount() {
        return objectTableModel == null ? super.getRowCount() : objectTableModel.getRowCount();
    }

    @Override
    public void fireTableDataChanged() {
        if (objectTableModel == null) {
            super.fireTableDataChanged();
        } else {
            objectTableModel.fireTableDataChanged();
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (objectTableModel == null) {
            return "";
        }

        return objectTableModel.getValueAt(rowIndex, columnIndex);
    }
}
