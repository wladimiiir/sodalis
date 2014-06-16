
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.enumeration;

import sk.magiksoft.sodalis.core.exception.VetoException;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.settings.SettingsPanel;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.swing.itemcomponent.ItemComponent;
import sk.magiksoft.swing.table.TextFieldCellEditor;

import javax.security.auth.Subject;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

/**
 * @author wladimiiir
 */
public class DefaultEnumerationSettingPanel extends JPanel implements SettingsPanel {

    private static final TableCellEditor TABLE_CELL_EDITOR_NAME = new TextFieldCellEditor();
    private Enumeration enumeration;
    private EnumerationEntryComponent enumerationEntryComponent;
    private EnumerationEntryTableModel tableModel = new EnumerationEntryTableModel();

    public DefaultEnumerationSettingPanel(Enumeration enumeration) {
        this.enumeration = (Enumeration) enumeration.clone();
        initComponents();
    }

    private void initComponents() {
        enumerationEntryComponent = new EnumerationEntryComponent();

        setLayout(new BorderLayout());
        add(enumerationEntryComponent, BorderLayout.CENTER);
    }

    @Override
    public String getSettingsPanelName() {
        return enumeration.getDescription();
    }

    @Override
    public JComponent getSwingComponent() {
        return this;
    }

    @Override
    public void reloadSettings() {
        enumeration.reloadEnumeration();
        tableModel.setObjects(enumeration.getEntries());
    }

    @Override
    public boolean saveSettings() {
        enumeration.getEntries().clear();
        enumeration.getEntries().addAll(tableModel.getObjects());
        enumeration.saveEnumeration();
        return true;
    }

    @Override
    public void setup(Subject subject) throws VetoException {
    }

    private class EnumerationEntryComponent extends ItemComponent {

        @Override
        protected Object getNewItem() {
            return new EnumerationEntry();
        }

        @Override
        protected ObjectTableModel createTableModel() {
            return DefaultEnumerationSettingPanel.this.tableModel;
        }

        @Override
        public TableCellEditor getCellEditor(int column) {
            switch (column) {
                case 0:
                    return TABLE_CELL_EDITOR_NAME;
                default:
                    return super.getCellEditor(column);
            }
        }
    }

    private class EnumerationEntryTableModel extends ObjectTableModel<EnumerationEntry> {

        public EnumerationEntryTableModel() {
            super(new String[]{LocaleManager.getString("name")});
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            EnumerationEntry entry = getObject(rowIndex);

            switch (columnIndex) {
                case 0:
                    return entry.getText();
                default:
                    return "";
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            EnumerationEntry entry = getObject(rowIndex);
            String value = aValue.toString();

            switch (columnIndex) {
                case 0:
                    entry.setText(value);
                    break;
            }
            fireTableDataChanged();
        }


        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }
    }
}