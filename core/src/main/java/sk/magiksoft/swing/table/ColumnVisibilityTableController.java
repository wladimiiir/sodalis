package sk.magiksoft.swing.table;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * @author wladimiiir
 */
public class ColumnVisibilityTableController implements MouseListener {

    private Map<Object, Boolean> columnVisibilityMap = new HashMap<Object, Boolean>();
    private Vector<TableColumn> tableColumns = new Vector<TableColumn>();
    private JPopupMenu popupMenu;
    private JTable table;

    public boolean isColumnVisible(TableColumn column) {
        return columnVisibilityMap.get(column);
    }

    public void initForTable(JTable table) {
        this.table = table;
        tableColumns.clear();
        columnVisibilityMap.clear();
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(i);
            tableColumns.add(tableColumn);
            columnVisibilityMap.put(tableColumn, Boolean.TRUE);
        }
    }

    private void removeAllTableColumns() {
        while (table.getColumnCount() > 0) {
            table.removeColumn(table.getColumnModel().getColumn(0));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            getPopupMenu().show((Component) e.getSource(), e.getX(), 0);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }


    public List<TableColumnSetting> getTableColumnSettings() {
        List<TableColumnSetting> settings = new ArrayList<TableColumnSetting>();
        TableColumnSetting setting;

        for (TableColumn tableColumn : tableColumns) {
            setting = new TableColumnSetting();
            setting.identifier = tableColumn.getIdentifier();
            setting.headerValue = tableColumn.getHeaderValue();
            setting.modelIndex = tableColumn.getModelIndex();
            setting.columnIndex = getColumnIndex(tableColumn);
            setting.visible = isColumnVisible(tableColumn);
            setting.width = tableColumn.getWidth();
            settings.add(setting);
        }

        return settings;
    }

    private int getColumnIndex(TableColumn column) {
        int index = 0;
        Enumeration<TableColumn> enumeration = table.getColumnModel().getColumns();

        while (enumeration.hasMoreElements()) {
            TableColumn tableColumn = enumeration.nextElement();
            if (column == tableColumn) {
                break;
            }
            index++;
        }

        return index;
    }

    public void setupTableColumnSettings(List<TableColumnSetting> settings) {
        TableColumn column;

        removeAllTableColumns();
        if (settings == null) {
            return;
        }

        Collections.sort(settings);
        for (TableColumnSetting tableColumnSetting : settings) {
            column = getTableColumn(tableColumnSetting.identifier);
            column.setPreferredWidth(tableColumnSetting.width);
            column.setWidth(tableColumnSetting.width);
            if (tableColumnSetting.visible) {
                table.addColumn(column);
            } else {
                columnVisibilityMap.put(column, Boolean.FALSE);
            }
        }
        table.revalidate();
        table.repaint();
    }

    private TableColumn getTableColumn(Object identifier) {
        for (TableColumn tableColumn : tableColumns) {
            if (tableColumn.getIdentifier().equals(identifier)) {
                return tableColumn;
            }
        }

        return null;
    }

    public void setTableColumnVisibility(TableColumn column, boolean visible, int columnIndex) {
        columnVisibilityMap.put(column, visible);

        if (visible) {
            table.addColumn(column);
            table.moveColumn(table.getColumnCount() - 1, columnIndex);
        } else {
            if (table.getColumnCount() == 1) {
                columnVisibilityMap.put(column, Boolean.TRUE);
                return;
            }
            table.removeColumn(column);
        }
    }

    private JPopupMenu getPopupMenu() {
        popupMenu = new JPopupMenu();
        int i = 0;
        for (final TableColumn tableColumn : tableColumns) {
            JCheckBoxMenuItem cbmi = new JCheckBoxMenuItem(tableColumn.getHeaderValue().toString());
            final int columnIndex = i++;
            cbmi.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    setTableColumnVisibility(tableColumn, !columnVisibilityMap.get(tableColumn).booleanValue(), columnIndex);
                    popupMenu.setVisible(false);
                }
            });
            cbmi.setSelected(columnVisibilityMap.get(tableColumn));
            popupMenu.add(cbmi);
        }


        return popupMenu;
    }

    public static class TableColumnSetting implements Serializable, Comparable<TableColumnSetting> {

        private static final long serialVersionUID = -1981532367126659976L;
        private Object identifier;
        private Object headerValue;
        private int modelIndex;
        private int columnIndex;
        private int width = -1;
        private boolean visible = true;

        public int getColumnIndex() {
            return columnIndex;
        }

        public Object getHeaderValue() {
            return headerValue;
        }

        public Object getIdentifier() {
            return identifier;
        }

        public int getModelIndex() {
            return modelIndex;
        }

        public boolean isVisible() {
            return visible;
        }

        public int getWidth() {
            return width;
        }

        public void setColumnIndex(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        public void setHeaderValue(Object headerValue) {
            this.headerValue = headerValue;
        }

        public void setIdentifier(Object identifier) {
            this.identifier = identifier;
        }

        public void setModelIndex(int modelIndex) {
            this.modelIndex = modelIndex;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        @Override
        public int compareTo(TableColumnSetting o) {
            return new Integer(columnIndex).compareTo(o.columnIndex);
        }
    }
}
