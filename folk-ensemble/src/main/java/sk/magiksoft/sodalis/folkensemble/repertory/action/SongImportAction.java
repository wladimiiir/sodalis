package sk.magiksoft.sodalis.folkensemble.repertory.action;

import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.action.AbstractImportAction;
import sk.magiksoft.sodalis.core.action.ActionMessage;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.folkensemble.repertory.data.RepertoryDataManager;
import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wladimiiir
 * @since 2010/8/5
 */
public class SongImportAction extends AbstractImportAction {
    @Override
    protected void importObjects(List objects) {
        List<Song> songs = new LinkedList<Song>();

        for (Object object : objects) {
            if (object instanceof Song) {
                songs.add((Song) object);
            }
        }

        RepertoryDataManager.getInstance().addOrUpdateEntities(songs);
        SodalisApplication.get().showMessage(LocaleManager.getString("songsImported"), songs.size());
    }

    @Override
    public ActionMessage getActionMessage(List objects) {
        return new ActionMessage(true, LocaleManager.getString("importSongs"));
    }
}
