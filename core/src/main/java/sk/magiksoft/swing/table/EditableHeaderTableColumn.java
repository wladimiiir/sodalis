package sk.magiksoft.swing.table;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

/**
 * @author wladimiiir
 * @since 2011/4/11
 */
public class EditableHeaderTableColumn extends TableColumn {

    protected TableCellEditor headerEditor;

    protected boolean isHeaderEditable;

    public EditableHeaderTableColumn() {
        setHeaderEditor(createDefaultHeaderEditor());
        isHeaderEditable = true;
    }

    public void setHeaderEditor(TableCellEditor headerEditor) {
        this.headerEditor = headerEditor;
    }

    public TableCellEditor getHeaderEditor() {
        return headerEditor;
    }

    public void setHeaderEditable(boolean isEditable) {
        isHeaderEditable = isEditable;
    }

    public boolean isHeaderEditable() {
        return isHeaderEditable;
    }

    public void copyValues(TableColumn base) {
        modelIndex = base.getModelIndex();
        identifier = base.getIdentifier();
        width = base.getWidth();
        minWidth = base.getMinWidth();
        setPreferredWidth(base.getPreferredWidth());
        maxWidth = base.getMaxWidth();
        headerRenderer = base.getHeaderRenderer();
        headerValue = base.getHeaderValue();
        cellRenderer = base.getCellRenderer();
        cellEditor = base.getCellEditor();
        isResizable = base.getResizable();
    }

    protected TableCellEditor createDefaultHeaderEditor() {
        return new DefaultCellEditor(new JTextField());
    }

}
