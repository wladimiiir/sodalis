package sk.magiksoft.sodalis.folkensemble.member.action;

import sk.magiksoft.sodalis.core.action.AbstractExportAction;
import sk.magiksoft.sodalis.core.action.ActionMessage;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.folkensemble.member.MemberContextManager;
import sk.magiksoft.sodalis.folkensemble.member.data.MemberDataManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class MemberExportAction extends AbstractExportAction {

    @Override
    public ActionMessage getActionMessage(List objects) {
        return new ActionMessage(true, LocaleManager.getString("exportMembers"));
    }

    @Override
    protected List<? extends Object> getExportItems(int exportType) {
        switch (exportType) {
            case EXPORT_TYPE_ALL:
                return MemberDataManager.getAllMembers(false);
            case EXPORT_TYPE_SELECTED:
                return MemberContextManager.getContext().getSelectedEntities();
            default:
                return new ArrayList<Object>(0);
        }
    }
}
