package sk.magiksoft.swing.combobox;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.Vector;

/**
 * @author wladimiiir
 */
public class ComboBoxCellEditor extends JComboBox implements TableCellEditor {

    private ChangeEvent event = new ChangeEvent(this);

    public ComboBoxCellEditor() {
        initListeners();
    }

    public ComboBoxCellEditor(Vector<?> items) {
        super(items);
    }

    public ComboBoxCellEditor(Object[] items) {
        super(items);
    }

    public ComboBoxCellEditor(ComboBoxModel aModel) {
        super(aModel);
    }

    private void initListeners() {
        addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                stopCellEditing();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        setSelectedItem(value);
        return this;
    }

    @Override
    public Object getCellEditorValue() {
        return getSelectedItem();
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return anEvent instanceof MouseEvent && ((MouseEvent) anEvent).getClickCount() == 2;
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
        Object[] listeners = listenerList.getListenerList();

        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == CellEditorListener.class) {
                listener = (CellEditorListener) listeners[i + 1];
                listener.editingStopped(event);
            }
        }
    }

    private void fireEditingCanceled() {
        CellEditorListener listener;
        Object[] listeners = listenerList.getListenerList();

        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == CellEditorListener.class) {
                listener = (CellEditorListener) listeners[i + 1];
                listener.editingCanceled(event);
            }
        }
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }
}
