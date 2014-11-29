package sk.magiksoft.sodalis.folkensemble.repertory.report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import sk.magiksoft.sodalis.core.printing.ObjectDataSource;
import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song;

import java.util.List;

/**
 * @author wladimiiir
 */
public class SongDataSource extends ObjectDataSource<Song> {

    public SongDataSource(List<Song> songs) {
        super(songs);
    }

    @Override
    public Object getFieldValue(JRField field) throws JRException {
        if (entity == null) {
            return "";
        }

        if (field.getName().equals("songName")) {
            return entity.getName();
        } else if (field.getName().equals("description")) {
            return entity.getDescription();
        } else if (field.getName().equals("songGenre")) {
            return entity.getGenre();
        } else if (field.getName().equals("region")) {
            return entity.getRegion();
        } else if (field.getName().equals("songDuration")) {
            return entity.getDurationString();
        } else {
            return "";
        }
    }

}
