package sk.magiksoft.sodalis.folkensemble.repertory.ui;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song;
import sk.magiksoft.swing.ISTable;

/**
 * @author wladimiiir
 */
public class SongTableModel extends ObjectTableModel<Song> {

    public SongTableModel() {
        super(new Object[]{
                LocaleManager.getString("songName"),
                LocaleManager.getString("description"),
                LocaleManager.getString("songGenre"),
                LocaleManager.getString("region"),
                LocaleManager.getString("duration")
        },
                new Class[]{
                        ISTable.LEFT_ALIGNMENT_CLASS,
                        ISTable.LEFT_ALIGNMENT_CLASS,
                        ISTable.LEFT_ALIGNMENT_CLASS,
                        ISTable.LEFT_ALIGNMENT_CLASS,
                        ISTable.RIGHT_ALIGNMENT_CLASS,
                });
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        final Song song = getObject(rowIndex);

        switch (columnIndex) {
            case 0:
                return song.getName();
            case 1:
                return song.getDescription();
            case 2:
                return song.getGenre();
            case 3:
                return song.getRegion();
            case 4:
                return song.getDurationString();
            default:
                return "";
        }
    }
}
