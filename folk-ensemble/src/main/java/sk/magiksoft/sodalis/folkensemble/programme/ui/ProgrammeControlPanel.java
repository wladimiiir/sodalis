package sk.magiksoft.sodalis.folkensemble.programme.ui;

import sk.magiksoft.sodalis.core.ui.controlpanel.DefaultControlPanel;
import sk.magiksoft.sodalis.folkensemble.programme.data.ProgrammeDataManager;
import sk.magiksoft.sodalis.folkensemble.programme.entity.Programme;

/**
 * @author wladimiiir
 */
public class ProgrammeControlPanel extends DefaultControlPanel {

    public ProgrammeControlPanel() {
        super("programme");
    }

    @Override
    protected void saveObject(Object object) {
        ProgrammeDataManager.getInstance().updateProgramme((Programme) object);
    }
}
