package sk.magiksoft.sodalis.core.settings.storage;

import sk.magiksoft.swing.table.ColumnVisibilityTableController.TableColumnSetting;

/**
 * @author wladimiiir
 */
public class ISTableState {

    private TableColumnSetting[] tableColumnSettings;

    public ISTableState() {
    }

    public ISTableState(TableColumnSetting[] tableColumnSettings) {
        this.tableColumnSettings = tableColumnSettings;
    }

    public TableColumnSetting[] getTableColumnSettings() {
        return tableColumnSettings;
    }

    public void setTableColumnSettings(TableColumnSetting[] tableColumnSettings) {
        this.tableColumnSettings = tableColumnSettings;
    }
}
