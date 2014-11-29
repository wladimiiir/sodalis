package sk.magiksoft.swing.table;

import sk.magiksoft.swing.DateSpinner;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.EventObject;

/**
 * @author wladimiiir
 */
public class DateSpinnerTableCellEditor extends DateSpinner implements TableCellEditor {

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof Calendar) {
            setValue((Calendar) value);
        }

        return this;
    }

    @Override
    public Object getCellEditorValue() {
        return getValue();
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return anEvent.getSource() instanceof MouseEvent
                && ((MouseEvent) anEvent.getSource()).getClickCount() == 2;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        fireEditingStopped();

        return true;
    }

    @Override
    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    private void fireEditingStopped() {
        CellEditorListener[] listeners = listenerList.getListeners(CellEditorListener.class);
        ChangeEvent e = new ChangeEvent(this);

        for (CellEditorListener cellEditorListener : listeners) {
            cellEditorListener.editingStopped(e);
        }
    }

    private void fireEditingCanceled() {
        CellEditorListener[] listeners = listenerList.getListeners(CellEditorListener.class);
        ChangeEvent e = new ChangeEvent(this);

        for (CellEditorListener cellEditorListener : listeners) {
            cellEditorListener.editingCanceled(e);
        }
    }
}
