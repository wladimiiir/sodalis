package sk.magiksoft.sodalis.folkensemble.repertory.ui;

import sk.magiksoft.sodalis.category.ui.CategorizedEntityInfoPanel;
import sk.magiksoft.sodalis.core.module.Module;
import sk.magiksoft.sodalis.folkensemble.repertory.RepertoryModule;

/**
 * @author wladimiiir
 */
public class CategorizedRepertoryInfoPanel extends CategorizedEntityInfoPanel {

    @Override
    protected Class<? extends Module> getModuleClass() {
        return RepertoryModule.class;
    }

}
