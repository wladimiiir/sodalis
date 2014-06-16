
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

import sk.magiksoft.swing.PopupTextField;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.List;

/**
 * @author wladimiiir
 */
public class PopupTextFieldCellEditor extends PopupTextField implements TableCellEditor {

    private EventListenerList listeners = new EventListenerList();

    public PopupTextFieldCellEditor(List objectList) {
        super(objectList);
        initListeners();
    }

    private void initListeners() {
        addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                if (e.getOppositeComponent() == popupMenu) {
                    return;
                }
                stopCellEditing();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof String) {
            setText(value.toString());
        }
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
        fireEditingStopped(new ChangeEvent(this));
        return true;
    }

    @Override
    public void cancelCellEditing() {
        fireEditingCanceled(new ChangeEvent(this));
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
        listeners.add(CellEditorListener.class, l);
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        listeners.remove(CellEditorListener.class, l);
    }

    private void fireEditingStopped(ChangeEvent e) {
        Object[] listenerList = listeners.getListenerList();
        for (int i = 0; i < listenerList.length; i++) {
            if (listenerList[i] == CellEditorListener.class) {
                ((CellEditorListener) listenerList[i + 1]).editingStopped(e);
            }
        }
    }

    private void fireEditingCanceled(ChangeEvent e) {
        Object[] listenerList = listeners.getListenerList();
        for (int i = 0; i < listenerList.length; i++) {
            if (listenerList[i] == CellEditorListener.class) {
                ((CellEditorListener) listenerList[i + 1]).editingStopped(e);
            }
        }
    }
}