package sk.magiksoft.swing.table;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * @author wladimiiir
 */
public class TextFieldCellEditor extends JTextField implements TableCellEditor {

    private EventListenerList eventListenerList = new EventListenerList();
    private ChangeEvent event = new ChangeEvent(this);

    public TextFieldCellEditor() {
        initListeners();
    }

    public TextFieldCellEditor(Document doc, String text, int columns) {
        super(doc, text, columns);
        initListeners();
    }

    public TextFieldCellEditor(String text, int columns) {
        super(text, columns);
        initListeners();
    }

    public TextFieldCellEditor(int columns) {
        super(columns);
        initListeners();
    }

    public TextFieldCellEditor(String text) {
        super(text);
        initListeners();
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
        setText(value.toString());
        return this;
    }

    @Override
    public Object getCellEditorValue() {
        return getText();
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent && ((MouseEvent) anEvent).getClickCount() != 2) {
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
