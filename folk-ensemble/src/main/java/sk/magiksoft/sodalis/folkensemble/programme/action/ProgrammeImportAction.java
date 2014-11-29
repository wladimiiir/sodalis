package sk.magiksoft.sodalis.folkensemble.programme.action;

import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.action.AbstractImportAction;
import sk.magiksoft.sodalis.core.action.ActionMessage;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.folkensemble.programme.entity.Programme;
import sk.magiksoft.sodalis.folkensemble.repertory.data.RepertoryDataManager;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wladimiiir
 * @since 2010/8/5
 */
public class ProgrammeImportAction extends AbstractImportAction {
    @Override
    protected void importObjects(List objects) {
        List<Programme> programmes = new LinkedList<Programme>();

        for (Object object : objects) {
            if (object instanceof Programme) {
                programmes.add((Programme) object);
            }
        }

        RepertoryDataManager.getInstance().addOrUpdateEntities(programmes);
        SodalisApplication.get().showMessage(LocaleManager.getString("programmesImported"), programmes.size());
    }

    @Override
    public ActionMessage getActionMessage(List objects) {
        return new ActionMessage(true, LocaleManager.getString("importProgrammes"));
    }
}
