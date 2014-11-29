package sk.magiksoft.sodalis.folkensemble.repertory.action;

import sk.magiksoft.sodalis.core.action.AbstractExportAction;
import sk.magiksoft.sodalis.core.action.ActionMessage;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.folkensemble.repertory.RepertoryContextManager;
import sk.magiksoft.sodalis.folkensemble.repertory.data.RepertoryDataManager;
import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 * @since 2010/8/5
 */
public class SongExportAction extends AbstractExportAction {
    @Override
    protected List<? extends Object> getExportItems(int exportType) {
        switch (exportType) {
            case EXPORT_TYPE_ALL:
                return RepertoryDataManager.getInstance().getDatabaseEntities(Song.class);
            case EXPORT_TYPE_SELECTED:
                return RepertoryContextManager.getInstance().getContext().getSelectedEntities();
            default:
                return new ArrayList<Object>(0);
        }
    }

    @Override
    public ActionMessage getActionMessage(List objects) {
        return new ActionMessage(true, LocaleManager.getString("exportSongs"));
    }
}
