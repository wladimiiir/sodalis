
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.settings;

import org.jdesktop.application.Application.ExitListener;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.security.event.LoginEvent;
import sk.magiksoft.swing.ISTable;

import javax.swing.table.TableColumn;
import java.io.Serializable;
import java.util.*;

/**
 * @author wladimiiir
 */
public class TableSettings extends Settings implements ExitListener {
    private static final long serialVersionUID = 3501693751054358548L;

    private transient static TableSettings instance = null;
    private transient Map<String, ISTable> tableMap = new HashMap<String, ISTable>();

    private TableSettings() {
        super(TableSettings.class.getName());
        instance = this;
        SodalisApplication.get().addExitListener(this);
    }

    public static synchronized TableSettings getInstance() {
        if (instance == null) {
            new TableSettings();
        }

        return instance;
    }


    @Override
    public void subjectLoggedOut(LoginEvent event) {
        saveTableSettings();
    }

    @Override
    protected Map<String, Object> getDefaultSettingsMap() {
        return new HashMap<String, Object>();
    }

    public void registerTable(String key, ISTable table) {
        tableMap.put(key, table);

        setupForTableColumnSettings((List<TableColumnSetting>) getValue(key), table);
    }

    public void setTableColumnSettings(String key, List<TableColumnSetting> settings) {
        setValue(key, settings);
    }

    private void saveTableSettings() {
        for (String key : tableMap.keySet()) {
            setValue(key, getTableColumnSettings(tableMap.get(key)));
        }
        save();
    }

    public List<TableColumnSetting> getTableColumnSettings(String key) {
        return (List<TableColumnSetting>) getValue(key);
    }

    public static List<TableColumnSetting> getTableColumnSettings(ISTable table) {
        List<TableColumnSetting> columnSettings = new ArrayList<TableColumnSetting>();
        Enumeration<TableColumn> enumeration = table.getColumnModel().getColumns();
        TableColumn column;
        TableColumnSetting columnSetting;
        int i = 0;

        while (enumeration.hasMoreElements()) {
            column = enumeration.nextElement();
            columnSetting = new TableColumnSetting(column.getIdentifier(), column.getModelIndex(), i++);
            columnSetting.width = column.getWidth();
            columnSettings.add(columnSetting);
        }

        return columnSettings;
    }

    public static void setupForTableColumnSettings(List<TableColumnSetting> settings, ISTable table) {
        TableColumn column;
        Enumeration<TableColumn> en;
        List<TableColumn> columnsToRemove = new ArrayList<TableColumn>();

        if (settings == null) {
            return;
        }

        Collections.sort(settings, new TableColumnColumnIndexComparator());
        for (TableColumnSetting tableColumnSetting : settings) {
            column = table.getColumn(tableColumnSetting.identifier);
            column.setPreferredWidth(tableColumnSetting.width);
            table.moveColumn(table.getColumnModel().getColumnIndex(tableColumnSetting.identifier),
                    tableColumnSetting.columnIndex);
        }
        en = table.getColumnModel().getColumns();
        columns:
        while (en.hasMoreElements()) {
            column = en.nextElement();
            for (TableColumnSetting tableColumnSetting : settings) {
                if (tableColumnSetting.identifier.equals(column.getIdentifier())) {
                    continue columns;
                }
            }
            columnsToRemove.add(column);
        }
        for (TableColumn tableColumn : columnsToRemove) {
            table.getColumnVisibilityTableController().setTableColumnVisibility(tableColumn, false, -1);
        }
    }

    @Override
    public boolean canExit(EventObject event) {
        return true;
    }

    @Override
    public void willExit(EventObject event) {
        saveTableSettings();
    }

    public static class TableColumnSetting implements Serializable {

        private static final long serialVersionUID = -1981532367126659976L;
        private Object identifier;
        private Object headerValue;
        private int modelIndex;
        private int columnIndex;
        private int width = -1;

        public TableColumnSetting(Object identifier, int modelIndex, int columnIndex) {
            this.identifier = identifier;
            this.modelIndex = modelIndex;
            this.columnIndex = columnIndex;
        }

        public TableColumnSetting(Object identifier, Object headerValue, int modelIndex, int columnIndex) {
            this.identifier = identifier;
            this.headerValue = headerValue;
            this.modelIndex = modelIndex;
            this.columnIndex = columnIndex;
        }
    }

    private static class TableColumnColumnIndexComparator implements Comparator<TableColumnSetting> {

        @Override
        public int compare(TableColumnSetting o1, TableColumnSetting o2) {
            return new Integer(o2.columnIndex).compareTo(o1.modelIndex);
        }
    }
}