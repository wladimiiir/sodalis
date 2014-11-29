package sk.magiksoft.sodalis.folkensemble.member.ui;

import sk.magiksoft.sodalis.core.enumeration.Enumeration;
import sk.magiksoft.sodalis.core.enumeration.EnumerationEntry;
import sk.magiksoft.sodalis.core.enumeration.EnumerationFactory;
import sk.magiksoft.sodalis.core.enumeration.Enumerations;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.sodalis.folkensemble.member.entity.EnsembleMemberHistory;
import sk.magiksoft.swing.itemcomponent.ItemComponent;
import sk.magiksoft.swing.table.ListTextFieldCellEditor;
import sk.magiksoft.swing.table.TextFieldCellEditor;

import javax.swing.table.TableCellEditor;

/**
 * @author wladimiiir
 */
public class FolkEnsembleHistoryComponent extends ItemComponent {

    private final Enumeration ENUMERATION_ENSEMBLE = EnumerationFactory.getInstance().getEnumeration(Enumerations.FOLK_ENSEMBLES);
    private final ListTextFieldCellEditor TABLE_CELL_EDITOR_ENSEMBLE = new ListTextFieldCellEditor(ENUMERATION_ENSEMBLE.getEntries());
    private final TableCellEditor TABLE_CELL_EDITOR_DURATION = new TextFieldCellEditor();
    private final TableCellEditor TABLE_CELL_EDITOR_GROUP = new TextFieldCellEditor();

    @Override
    protected Object getNewItem() {
        return new EnsembleMemberHistory();
    }

    @Override
    protected ObjectTableModel createTableModel() {
        return new FolkEnsembleHistoryTableModel();
    }

    private void saveEnsembleEnumeration() {
        ENUMERATION_ENSEMBLE.saveEnumeration();
    }

    private class FolkEnsembleHistoryTableModel extends ObjectTableModel<EnsembleMemberHistory> {

        public FolkEnsembleHistoryTableModel() {
            super(new String[]{
                    LocaleManager.getString(FolkEnsembleHistoryComponent.class.getName() + ".ensemble"),
                    LocaleManager.getString(FolkEnsembleHistoryComponent.class.getName() + ".duration"),
                    LocaleManager.getString(FolkEnsembleHistoryComponent.class.getName() + ".group")
            });
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            EnsembleMemberHistory memberHistory = getObject(rowIndex);
            switch (columnIndex) {
                case 0:
                    return memberHistory.getEnsembleName();
                case 1:
                    return memberHistory.getPeriod();
                case 2:
                    return memberHistory.getPosition();
                default:
                    return "";
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            EnsembleMemberHistory memberHistory = getObject(rowIndex);
            switch (columnIndex) {
                case 0:
                    memberHistory.setEnsembleName(aValue.toString());
                    TABLE_CELL_EDITOR_ENSEMBLE.addItem(aValue.toString());
                    ENUMERATION_ENSEMBLE.addEntry(new EnumerationEntry(aValue.toString()));
                    saveEnsembleEnumeration();
                    break;
                case 1:
                    memberHistory.setPeriod(aValue.toString());
                    break;
                case 2:
                    memberHistory.setPosition(aValue.toString());
                    break;
                default:
                    super.setValueAt(aValue, rowIndex, columnIndex);
                    return;
            }
            fireTableDataChanged();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }
    }

    @Override
    public TableCellEditor getCellEditor(int column) {
        switch (column) {
            case 0:
                return TABLE_CELL_EDITOR_ENSEMBLE;
            case 1:
                return TABLE_CELL_EDITOR_DURATION;
            case 2:
                return TABLE_CELL_EDITOR_GROUP;
            default:
                return super.getCellEditor(column);
        }
    }
}
