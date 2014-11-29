package sk.magiksoft.swing.table;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * @author wladimiiir
 */
public class CheckBoxCellEditor extends JCheckBox implements TableCellEditor {

    private EventListenerList eventListenerList = new EventListenerList();
    private ChangeEvent event = new ChangeEvent(this);

    public CheckBoxCellEditor() {
        setOpaque(false);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        initListeners();
    }

    private void initListeners() {
        addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                stopCellEditing();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof Boolean) {
            setSelected((Boolean) value);
        } else if (value != null) {
            setSelected(Boolean.valueOf(value.toString()));
        } else {
            setSelected(false);
        }
        return this;
    }

    @Override
    public Object getCellEditorValue() {
        return isSelected();
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent && ((MouseEvent) anEvent).getClickCount() != 1) {
            return false;
        }
        return true;
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

    private void fireEditingStopped() {
        CellEditorListener listener;
        Object[] listeners = eventListenerList.getListenerList();

        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == CellEditorListener.class) {
                listener = (CellEditorListener) listeners[i + 1];
                listener.editingStopped(event);
            }
        }
    }

    private void fireEditingCanceled() {
        CellEditorListener listener;
        Object[] listeners = eventListenerList.getListenerList();

        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == CellEditorListener.class) {
                listener = (CellEditorListener) listeners[i + 1];
                listener.editingCanceled(event);
            }
        }
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
        eventListenerList.add(CellEditorListener.class, l);
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        eventListenerList.remove(CellEditorListener.class, l);
    }
}
