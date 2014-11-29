package sk.magiksoft.sodalis.folkensemble.programme.report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import sk.magiksoft.sodalis.core.printing.ObjectDataSource;
import sk.magiksoft.sodalis.folkensemble.programme.entity.Programme;

import java.util.List;

/**
 * @author wladimiiir
 */
public class ProgrammeDataSource extends ObjectDataSource<Programme> {

    public ProgrammeDataSource(List<Programme> songs) {
        super(songs);
    }

    @Override
    public Object getFieldValue(JRField field) throws JRException {
        if (entity == null) {
            return "";
        }

        if (field.getName().equals("programmeName")) {
            return entity.getName();
        } else if (field.getName().equals("description")) {
            return entity.getDescription();
        } else if (field.getName().equals("programmeDuration")) {
            return entity.getDurationString();
        } else {
            return "";
        }
    }

}
