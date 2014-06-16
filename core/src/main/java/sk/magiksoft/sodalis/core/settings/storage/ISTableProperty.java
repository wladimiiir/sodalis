
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

import org.jdesktop.application.session.PropertySupport;
import sk.magiksoft.swing.ISTable;
import sk.magiksoft.swing.table.ColumnVisibilityTableController.TableColumnSetting;

import java.awt.*;
import java.util.Arrays;

/**
 * @author wladimiiir
 */
public class ISTableProperty implements PropertySupport {

    @Override
    public Object getSessionState(Component c) {
        if (!(c instanceof ISTable)) {
            return null;
        }

        ISTable table = (ISTable) c;
        return new ISTableState(table.getColumnVisibilityTableController()
                .getTableColumnSettings().toArray(new TableColumnSetting[table.getColumnVisibilityTableController()
                        .getTableColumnSettings().size()]));
    }

    @Override
    public void setSessionState(Component c, Object state) {
        if (!(c instanceof ISTable) || !(state instanceof ISTableState)) {
            return;
        }

        ISTable table = (ISTable) c;
        ISTableState tableState = (ISTableState) state;

        table.getColumnVisibilityTableController().setupTableColumnSettings(Arrays.asList(tableState.getTableColumnSettings()));
    }
}