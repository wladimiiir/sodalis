package sk.magiksoft.sodalis.folkensemble.programme.data;

import sk.magiksoft.sodalis.category.CategoryManager;
import sk.magiksoft.sodalis.core.data.remote.client.ClientDataManager;
import sk.magiksoft.sodalis.folkensemble.programme.ProgrammeModule;
import sk.magiksoft.sodalis.folkensemble.programme.entity.Programme;
import sk.magiksoft.sodalis.person.entity.PersonWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class ProgrammeDataManager extends ClientDataManager {

    private static ProgrammeDataManager instance;

    private ProgrammeDataManager() {
    }

    public static synchronized ProgrammeDataManager getInstance() {
        if (instance == null) {
            instance = new ProgrammeDataManager();
        }
        return instance;
    }

    public void updateProgramme(Programme programme) {
        final String sessionID = registerNewSession();

        List<PersonWrapper> personWrappers = getDatabaseEntities(sessionID, "select a from Programme p join p.authors as a where p.id=" + programme.getId() + ")");
        personWrappers.addAll(getDatabaseEntities(sessionID, "select c from Programme p join p.composers as c where p.id=" + programme.getId() + ")"));
        personWrappers.addAll(getDatabaseEntities(sessionID, "select ch from Programme p join p.choreographers as ch where p.id=" + programme.getId() + ")"));
        for (PersonWrapper personWrapper : personWrappers) {
            removeDatabaseEntity(sessionID, personWrapper);
        }
        programme.setAuthors(new ArrayList<PersonWrapper>(programme.getAuthors()));
        programme.setComposers(new ArrayList<PersonWrapper>(programme.getComposers()));
        programme.setChoreographers(new ArrayList<PersonWrapper>(programme.getChoreographers()));

        for (PersonWrapper personWrapper : programme.getAuthors()) {
            personWrapper.setId(null);
        }
        for (PersonWrapper personWrapper : programme.getComposers()) {
            personWrapper.setId(null);
        }
        for (PersonWrapper personWrapper : programme.getChoreographers()) {
            personWrapper.setId(null);
        }

        updateDatabaseEntity(sessionID, programme);
        updateDatabaseEntity(sessionID, CategoryManager.getInstance().getRootCategory(ProgrammeModule.class, false));
        closeSession(sessionID);
    }
}
