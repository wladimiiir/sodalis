
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.person.ui;

import sk.magiksoft.sodalis.core.enumeration.Enumeration;
import sk.magiksoft.sodalis.core.enumeration.TypedEnumerationEntry;
import sk.magiksoft.sodalis.core.exception.VetoException;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.settings.SettingsPanel;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.sodalis.person.entity.PersonTitleEnumerationInfo;
import sk.magiksoft.swing.combobox.ComboBoxCellEditor;
import sk.magiksoft.swing.itemcomponent.ItemComponent;
import sk.magiksoft.swing.table.TextFieldCellEditor;

import javax.security.auth.Subject;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

/**
 * @author wladimiiir
 */
public class PersonTitleEnumerationSettingPanel extends JPanel implements SettingsPanel {

    private static final TableCellEditor TABLE_CELL_EDITOR_NAME = new TextFieldCellEditor();
    private static final TableCellEditor TABLE_CELL_EDITOR_TYPE = new ComboBoxCellEditor(new Object[]{
            new PersonTitleTypeItem(PersonTitleEnumerationInfo.TYPE_BEFORE_NAME),
            new PersonTitleTypeItem(PersonTitleEnumerationInfo.TYPE_AFTER_NAME)
    }) {

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            JComboBox comboBox = (JComboBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);

            if (value instanceof String) {
                for (int i = 0; i < comboBox.getItemCount(); i++) {
                    PersonTitleTypeItem item = (PersonTitleTypeItem) comboBox.getItemAt(i);
                    if (item.toString().equals(value)) {
                        comboBox.setSelectedItem(item);
                        break;
                    }
                }
            } else if (value instanceof PersonTitleTypeItem) {
                comboBox.setSelectedItem(value);
            }

            return comboBox;
        }

    };
    private Enumeration enumeration;
    private PersonTitleEnumerationComponent component;
    private ObjectTableModel tableModel;

    public PersonTitleEnumerationSettingPanel(Enumeration enumeration) {
        this.enumeration = (Enumeration) enumeration.clone();
        initComponents();
    }

    private void initComponents() {
        component = new PersonTitleEnumerationComponent();
        setLayout(new BorderLayout());
        add(component, BorderLayout.CENTER);
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
        enumeration.setEntries(tableModel.getObjects());
        enumeration.saveEnumeration();
        return true;
    }

    @Override
    public void setup(Subject subject) throws VetoException {
    }

    private class PersonTitleEnumerationComponent extends ItemComponent {

        @Override
        protected Object getNewItem() {
            final TypedEnumerationEntry entry = new TypedEnumerationEntry();

            entry.setType(PersonTitleEnumerationInfo.TYPE_BEFORE_NAME);
            return entry;
        }

        @Override
        protected ObjectTableModel createTableModel() {
            return PersonTitleEnumerationSettingPanel.this.tableModel = new PersonTitleTableModel();
        }

        @Override
        protected TableCellEditor getCellEditor(int column) {
            switch (column) {
                case 0:
                    return TABLE_CELL_EDITOR_NAME;
                case 1:
                    return TABLE_CELL_EDITOR_TYPE;
            }
            return super.getCellEditor(column);
        }
    }

    private class PersonTitleTableModel extends ObjectTableModel<TypedEnumerationEntry> {

        public PersonTitleTableModel() {
            super(new String[]{
                    LocaleManager.getString("title"),
                    LocaleManager.getString("position")
            });
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            TypedEnumerationEntry entry = getObject(rowIndex);

            switch (columnIndex) {
                case 0:
                    return entry.getText();
                case 1:
                    return LocaleManager.getString(entry.getType());
            }

            return "";
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            TypedEnumerationEntry entry = getObject(rowIndex);

            switch (columnIndex) {
                case 0:
                    entry.setText(aValue.toString());
                    break;
                case 1:
                    entry.setType(((PersonTitleTypeItem) aValue).type);
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }
    }

    private static class PersonTitleTypeItem {

        private String type;

        public PersonTitleTypeItem(String type) {
            this.type = type;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj instanceof String) {
                String other = (String) obj;

                if ((this.type == null) ? (other != null)
                        : LocaleManager.getString(this.type).equals(other)) {
                    return false;
                }
                return true;
            } else if (obj instanceof PersonTitleTypeItem) {
                PersonTitleTypeItem other = (PersonTitleTypeItem) obj;

                if ((this.type == null) ? (other.type != null) : !this.type.equals(other.type)) {
                    return false;
                }
                return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 59 * hash + (this.type != null ? this.type.hashCode() : 0);
            return hash;
        }

        @Override
        public String toString() {
            return LocaleManager.getString(type);
        }
    }
}