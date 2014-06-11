
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
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
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Aug 5, 2010
 * Time: 11:17:13 AM
 * To change this template use File | Settings | File Templates.
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