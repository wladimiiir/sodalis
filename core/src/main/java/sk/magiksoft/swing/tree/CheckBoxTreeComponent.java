
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.swing.tree;

import sk.magiksoft.sodalis.core.locale.LocaleManager;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * @author wladimiiir
 */
public class CheckBoxTreeComponent extends JCheckBox implements TreeCellEditor, TreeCellRenderer, ActionListener {

    private JTree tree;
    private int editingRow;

    public CheckBoxTreeComponent() {
        setOpaque(false);
        setFocusPainted(false);
        addActionListener(this);
    }

    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        setText(value.toString());
        setSelected(isSelected);

        if (this.tree == null) {
            this.tree = tree;
            this.tree.setToolTipText(LocaleManager.getString("CheckBoxTreeComponent.tooltip"));
        }
        this.editingRow = row;

        return this;
    }

    @Override
    public Object getCellEditorValue() {
        return new CheckObject(getText(), isSelected());
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return false;
    }

    @Override
    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    @Override
    public void cancelCellEditing() {
        fireEditingCancelled();
    }

    private void fireEditingStopped() {
        ChangeEvent event = new ChangeEvent(this);
        CellEditorListener[] listeners = listenerList.getListeners(CellEditorListener.class);

        for (CellEditorListener cellEditorListener : listeners) {
            cellEditorListener.editingStopped(event);
        }
    }

    private void fireEditingCancelled() {
        ChangeEvent event = new ChangeEvent(this);
        CellEditorListener[] listeners = listenerList.getListeners(CellEditorListener.class);

        for (CellEditorListener cellEditorListener : listeners) {
            cellEditorListener.editingCanceled(event);
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

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        setText(value.toString());
        setSelected(selected);

        return this;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (tree == null) {
            return;
        }

        if (isSelected()) {
            if (e.getModifiers() == (KeyEvent.CTRL_MASK + MouseEvent.BUTTON1_MASK)) {
                tree.getSelectionModel().addSelectionPath(tree.getPathForRow(editingRow));
            } else {
                tree.getSelectionModel().setSelectionPath(tree.getPathForRow(editingRow));
            }
        } else {
            tree.getSelectionModel().removeSelectionPath(tree.getPathForRow(editingRow));
        }
    }
}