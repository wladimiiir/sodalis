package sk.magiksoft.sodalis.folkensemble.programme.ui;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.sodalis.folkensemble.programme.entity.Programme;
import sk.magiksoft.swing.ISTable;

/**
 * @author wladimiiir
 */
public class ProgrammeTableModel extends ObjectTableModel<Programme> {

    public ProgrammeTableModel() {
        super(new Object[]{
                LocaleManager.getString("programmeName"),
                LocaleManager.getString("description"),
                LocaleManager.getString("duration")
        },
                new Class[]{
                        ISTable.LEFT_ALIGNMENT_CLASS,
                        ISTable.LEFT_ALIGNMENT_CLASS,
                        ISTable.RIGHT_ALIGNMENT_CLASS
                });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Programme programme = getObject(rowIndex);

        switch (columnIndex) {
            case 0:
                return programme.getName();
            case 1:
                return programme.getDescription();
            case 2:
                return programme.getDurationString();
            default:
                return "";
        }
    }
}
