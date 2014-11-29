package sk.magiksoft.sodalis.folkensemble.inventory.ui;

import sk.magiksoft.sodalis.category.ui.CategorizedEntityInfoPanel;
import sk.magiksoft.sodalis.core.module.Module;
import sk.magiksoft.sodalis.folkensemble.inventory.InventoryModule;

/**
 * @author wladimiiir
 */
public class CategorizedInventoryInfoPanel extends CategorizedEntityInfoPanel {

    @Override
    protected Class<? extends Module> getModuleClass() {
        return InventoryModule.class;
    }

}
