package sk.magiksoft.sodalis.folkensemble.programme.ui;

import sk.magiksoft.sodalis.category.ui.CategorizedEntityInfoPanel;
import sk.magiksoft.sodalis.core.module.Module;
import sk.magiksoft.sodalis.folkensemble.programme.ProgrammeModule;

/**
 * @author wladimiiir
 */
public class CategorizedProgrammeInfoPanel extends CategorizedEntityInfoPanel {

    @Override
    protected Class<? extends Module> getModuleClass() {
        return ProgrammeModule.class;
    }

}
