package sk.magiksoft.sodalis.event.ui;

import sk.magiksoft.sodalis.category.ui.CategorizedEntityInfoPanel;
import sk.magiksoft.sodalis.core.module.Module;
import sk.magiksoft.sodalis.event.EventModule;

/**
 * @author wladimiiir
 */
public class CategorizedEventInfoPanel extends CategorizedEntityInfoPanel {

    @Override
    protected Class<? extends Module> getModuleClass() {
        return EventModule.class;
    }

}
