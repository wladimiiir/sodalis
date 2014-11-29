package sk.magiksoft.sodalis.folkensemble.programme.ui;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.sodalis.folkensemble.programme.entity.ProgrammeSong;
import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song;

import javax.swing.table.DefaultTableModel;

/**
 * @author wladimiiir
 */
public class SongTotalTimeTableModel extends DefaultTableModel {

    private ObjectTableModel<ProgrammeSong> programmeSongTableModel;

    public SongTotalTimeTableModel(ObjectTableModel<ProgrammeSong> programmeSongTableModel) {
        super(1, 2);
        this.programmeSongTableModel = programmeSongTableModel;
    }

    private String getTotalSongDuration() {
        int totalDuration = 0;

        for (ProgrammeSong programmeSong : programmeSongTableModel.getObjects()) {
            totalDuration += programmeSong.getSong().getDuration();
        }

        return Song.DURATION_FORMAT.format(totalDuration / 3600) + ":" + Song.DURATION_FORMAT.format(
                totalDuration / 60) + ":" + Song.DURATION_FORMAT.format(totalDuration % 60);
    }

    @Override
    public Object getValueAt(int row, int column) {
        switch (column) {
            case 0:
                return LocaleManager.getString("totalTime");
            case 1:
                return getTotalSongDuration();
            default:
                return "";
        }

    }
}
