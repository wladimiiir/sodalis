package sk.magiksoft.sodalis.folkensemble.event.ui;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.sodalis.folkensemble.programme.entity.ProgrammeSong;
import sk.magiksoft.swing.ISTable;

/**
 * @author wladimiiir
 */
public class ProgrammeTableModel extends ObjectTableModel<ProgrammeSong> {

    public ProgrammeTableModel() {
        super(new String[]{
                LocaleManager.getString("order"),
                LocaleManager.getString("songName"),
                LocaleManager.getString("duration")
        }, new Class[]{
                ISTable.RIGHT_ALIGNMENT_CLASS,
                ISTable.LEFT_ALIGNMENT_CLASS,
                ISTable.RIGHT_ALIGNMENT_CLASS
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ProgrammeSong song = getObject(rowIndex);

        switch (columnIndex) {
            case 0:
                return (rowIndex + 1) + ".";
            case 1:
                return song.getSong().getName();
            case 2:
                return song.getSong().getDurationString();
            default:
                return "";
        }
    }

}
