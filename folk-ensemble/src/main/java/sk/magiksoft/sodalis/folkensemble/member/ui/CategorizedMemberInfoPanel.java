package sk.magiksoft.sodalis.folkensemble.member.ui;

import sk.magiksoft.sodalis.category.ui.CategorizedEntityInfoPanel;
import sk.magiksoft.sodalis.core.module.Module;
import sk.magiksoft.sodalis.folkensemble.member.MemberModule;

/**
 * @author wladimiiir
 */
public class CategorizedMemberInfoPanel extends CategorizedEntityInfoPanel {

    @Override
    protected Class<? extends Module> getModuleClass() {
        return MemberModule.class;
    }

    @Override
    public boolean isWizardSupported() {
        return false;
    }
}
