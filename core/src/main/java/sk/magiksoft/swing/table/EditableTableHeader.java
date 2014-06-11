/*
 * Copyright (c) 2011
 */

package sk.magiksoft.swing.table;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.EventObject;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 4/11/11
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class EditableTableHeader extends JTableHeader implements CellEditorListener {
    public final int HEADER_ROW = -10;

    transient protected int editingColumn;

    transient protected TableCellEditor cellEditor;

    transient protected Component editorComp;

    public EditableTableHeader(TableColumnModel columnModel) {
        super(columnModel);
        setReorderingAllowed(false);
        cellEditor = null;
        recreateTableColumn(columnModel);
    }

    public void updateUI() {
        setUI(new EditableTableHeaderUI());
        resizeAndRepaint();
        invalidate();
    }

    protected void recreateTableColumn(TableColumnModel columnModel) {
        int n = columnModel.getColumnCount();
        EditableHeaderTableColumn[] newCols = new EditableHeaderTableColumn[n];
        TableColumn[] oldCols = new TableColumn[n];
        for (int i = 0; i < n; i++) {
            oldCols[i] = columnModel.getColumn(i);
            newCols[i] = new EditableHeaderTableColumn();
            newCols[i].copyValues(oldCols[i]);
        }
        for (int i = 0; i < n; i++) {
            columnModel.removeColumn(oldCols[i]);
        }
        for (int i = 0; i < n; i++) {
            columnModel.addColumn(newCols[i]);
        }
    }

    public boolean editCellAt(int index, EventObject e) {
        if (cellEditor != null && !cellEditor.stopCellEditing()) {
            return false;
        }
        if (!isCellEditable(index)) {
            return false;
        }
        TableCellEditor editor = getCellEditor(index);

        if (editor != null && editor.isCellEditable(e)) {
            editorComp = prepareEditor(editor, index);
            editorComp.setBounds(getHeaderRect(index));
            add(editorComp);
            editorComp.validate();
            setCellEditor(editor);
            setEditingColumn(index);
            editor.addCellEditorListener(this);

            return true;
        }
        return false;
    }

    public boolean isCellEditable(int index) {
        if (getReorderingAllowed()) {
            return false;
        }
        int columnIndex = columnModel.getColumn(index).getModelIndex();
        EditableHeaderTableColumn col = (EditableHeaderTableColumn) columnModel
                .getColumn(columnIndex);
        return col.isHeaderEditable();
    }

    public TableCellEditor getCellEditor(int index) {
        int columnIndex = columnModel.getColumn(index).getModelIndex();
        EditableHeaderTableColumn col = (EditableHeaderTableColumn) columnModel
                .getColumn(columnIndex);
        return col.getHeaderEditor();
    }

    public void setCellEditor(TableCellEditor newEditor) {
        TableCellEditor oldEditor = cellEditor;
        cellEditor = newEditor;

        // firePropertyChange

        if (oldEditor != null) {
            ((TableCellEditor) oldEditor)
                    .removeCellEditorListener((CellEditorListener) this);
        }
        if (newEditor != null) {
            ((TableCellEditor) newEditor)
                    .addCellEditorListener((CellEditorListener) this);
        }
    }

    public Component prepareEditor(TableCellEditor editor, int index) {
        Object value = columnModel.getColumn(index).getHeaderValue();
        boolean isSelected = true;
        int row = HEADER_ROW;
        JTable table = getTable();
        Component comp = editor.getTableCellEditorComponent(table, value,
                isSelected, row, index);
        if (comp instanceof JComponent) {
            ((JComponent) comp).setNextFocusableComponent(this);
        }
        return comp;
    }

    public TableCellEditor getCellEditor() {
        return cellEditor;
    }

    public Component getEditorComponent() {
        return editorComp;
    }

    public void setEditingColumn(int aColumn) {
        editingColumn = aColumn;
    }

    public int getEditingColumn() {
        return editingColumn;
    }

    public void removeEditor() {
        TableCellEditor editor = getCellEditor();
        if (editor != null) {
            editor.removeCellEditorListener(this);

            requestFocus();
            remove(editorComp);

            int index = getEditingColumn();
            Rectangle cellRect = getHeaderRect(index);

            setCellEditor(null);
            setEditingColumn(-1);
            editorComp = null;

            repaint(cellRect);
        }
    }

    public boolean isEditing() {
        return (cellEditor != null);
    }

    //
    // CellEditorListener
    //
    public void editingStopped(ChangeEvent e) {
        TableCellEditor editor = getCellEditor();
        if (editor != null) {
            Object value = editor.getCellEditorValue();
            int index = getEditingColumn();
            columnModel.getColumn(index).setHeaderValue(value);
            removeEditor();
        }
    }

    public void editingCanceled(ChangeEvent e) {
        removeEditor();
    }

    //
    // public void setReorderingAllowed(boolean b) {
    //   reorderingAllowed = false;
    // }

}
