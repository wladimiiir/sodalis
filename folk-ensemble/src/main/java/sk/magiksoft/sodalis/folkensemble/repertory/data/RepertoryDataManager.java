package sk.magiksoft.sodalis.folkensemble.repertory.data;

import sk.magiksoft.sodalis.category.CategoryManager;
import sk.magiksoft.sodalis.core.data.remote.client.ClientDataManager;
import sk.magiksoft.sodalis.folkensemble.repertory.RepertoryModule;
import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song;
import sk.magiksoft.sodalis.person.entity.PersonWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class RepertoryDataManager extends ClientDataManager {

    private static RepertoryDataManager instance;

    private RepertoryDataManager() {
    }

    public static synchronized RepertoryDataManager getInstance() {
        if (instance == null) {
            instance = new RepertoryDataManager();
        }
        return instance;
    }

    public void updateSong(Song song) {
        final String sessionID = registerNewSession();

        List<PersonWrapper> personWrappers = getDatabaseEntities(sessionID, "select c from Song s join s.composers as c where s.id=" + song.getId() + ")");
        personWrappers.addAll(getDatabaseEntities(sessionID, "select c from Song s join s.choreographers as c where s.id=" + song.getId() + ")"));
        personWrappers.addAll(getDatabaseEntities(sessionID, "select i from Song s join s.interpreters as i where s.id=" + song.getId() + ")"));
        personWrappers.addAll(getDatabaseEntities(sessionID, "select p from Song s join s.pedagogists as p where s.id=" + song.getId() + ")"));
        for (PersonWrapper personWrapper : personWrappers) {
            removeDatabaseEntity(sessionID, personWrapper);
        }
        song.setChoreographers(new ArrayList<PersonWrapper>(song.getChoreographers()));
        song.setComposers(new ArrayList<PersonWrapper>(song.getComposers()));
        song.setInterpreters(new ArrayList<PersonWrapper>(song.getInterpreters()));
        song.setPedagogists(new ArrayList<PersonWrapper>(song.getPedagogists()));

        for (PersonWrapper personWrapper : song.getChoreographers()) {
            personWrapper.setId(null);
        }
        for (PersonWrapper personWrapper : song.getComposers()) {
            personWrapper.setId(null);
        }
        for (PersonWrapper personWrapper : song.getInterpreters()) {
            personWrapper.setId(null);
        }
        for (PersonWrapper personWrapper : song.getPedagogists()) {
            personWrapper.setId(null);
        }

        updateDatabaseEntity(sessionID, song);
        updateDatabaseEntity(sessionID, CategoryManager.getInstance().getRootCategory(RepertoryModule.class, false));
        closeSession(sessionID);
    }
}
