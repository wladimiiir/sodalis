
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.swing.table;

import sk.magiksoft.swing.MultiSelectComboBox;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.List;
import java.util.Vector;

/**
 * @author wladimiiir
 */
public class MultiselectComboBoxTableCellEditor extends MultiSelectComboBox implements TableCellEditor {
    private EventListenerList eventListenerList = new EventListenerList();
    private ChangeEvent event = new ChangeEvent(this);

    public MultiselectComboBoxTableCellEditor() {
    }

    public MultiselectComboBoxTableCellEditor(Vector<?> items) {
        super(items);
    }

    @Override
    protected void hidePopup() {
        super.hidePopup();
        stopCellEditing();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        setSelectedObjects((List) value);
        return this;
    }

    @Override
    public Object getCellEditorValue() {
        return getSelectedObjects();
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