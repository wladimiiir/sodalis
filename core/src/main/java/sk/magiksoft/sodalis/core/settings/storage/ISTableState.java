
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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