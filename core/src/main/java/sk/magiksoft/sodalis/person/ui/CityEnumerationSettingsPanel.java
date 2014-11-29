package sk.magiksoft.sodalis.person.ui;

import sk.magiksoft.sodalis.core.enumeration.Enumeration;
import sk.magiksoft.sodalis.core.exception.VetoException;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.settings.SettingsPanel;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.sodalis.core.utils.CollectionUtils;
import sk.magiksoft.sodalis.person.entity.CityEnumerationEntry;
import sk.magiksoft.swing.itemcomponent.ItemComponent;
import sk.magiksoft.swing.table.TextFieldCellEditor;

import javax.security.auth.Subject;
import javax.swing.*;
import javax.swing.table.TableCellEditor;

/**
 * @author wladimiiir
 * @since 2010/11/28
 */
public class CityEnumerationSettingsPanel implements SettingsPanel {
    private static final TextFieldCellEditor POSTCODE_CELL_EDITOR = new TextFieldCellEditor();
    private static final TextFieldCellEditor CITY_CELL_EDITOR = POSTCODE_CELL_EDITOR;
    private Enumeration enumeration;
    private CityEnumerationEntryComponent entryComponent;


    public CityEnumerationSettingsPanel(Enumeration enumeration) {
        this.enumeration = enumeration;
    }

    @Override
    public String getSettingsPanelName() {
        return enumeration.getDescription();
    }

    @Override
    public JComponent getSwingComponent() {
        if (entryComponent == null) {
            entryComponent = new CityEnumerationEntryComponent();
        }

        return entryComponent;
    }

    @Override
    public void setup(Subject subject) throws VetoException {
    }

    @Override
    public void reloadSettings() {
        enumeration.reloadEnumeration();
        entryComponent.setItems(CollectionUtils.filter(enumeration.getEntries(), CityEnumerationEntry.class));
    }

    @Override
    public boolean saveSettings() {
        enumeration.getEntries().clear();
        enumeration.getEntries().addAll(entryComponent.getItems());
        enumeration.saveEnumeration();

        return true;
    }

    private class CityEnumerationEntryComponent extends ItemComponent<CityEnumerationEntry> {

        @Override
        protected CityEnumerationEntry getNewItem() {
            return new CityEnumerationEntry();
        }

        @Override
        protected TableCellEditor getCellEditor(int column) {
            switch (column) {
                case 0:
                    return CITY_CELL_EDITOR;
                case 1:
                    return POSTCODE_CELL_EDITOR;
            }

            return null;
        }

        @Override
        protected ObjectTableModel<CityEnumerationEntry> createTableModel() {
            return new CityEnumerationEntryTableModel();
        }

        private class CityEnumerationEntryTableModel extends ObjectTableModel<CityEnumerationEntry> {

            public CityEnumerationEntryTableModel() {
                super(new Object[]{
                        LocaleManager.getString("town"),
                        LocaleManager.getString("postcode")
                });
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                final CityEnumerationEntry entry = getObject(rowIndex);

                switch (columnIndex) {
                    case 0:
                        return entry.getText();
                    case 1:
                        return entry.getZipCode();
                }

                return null;
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                final CityEnumerationEntry entry = getObject(rowIndex);

                switch (columnIndex) {
                    case 0:
                        entry.setText(aValue == null ? "" : aValue.toString());
                        break;
                    case 1:
                        entry.setZipCode(aValue == null ? "" : aValue.toString());
                        break;
                }
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return true;
            }
        }
    }
}
