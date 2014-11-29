package sk.magiksoft.sodalis.folkensemble.programme;

import sk.magiksoft.sodalis.core.context.AbstractContextManager;
import sk.magiksoft.sodalis.core.context.Context;
import sk.magiksoft.sodalis.core.data.remote.server.DataManager;
import sk.magiksoft.sodalis.core.utils.Utils;
import sk.magiksoft.sodalis.folkensemble.programme.entity.Programme;
import sk.magiksoft.sodalis.folkensemble.programme.ui.ProgrammeUI;
import sk.magiksoft.sodalis.folkensemble.repertory.data.RepertoryDataManager;

import java.net.URL;

/**
 * @author wladimiiir
 */
public class ProgrammeContextManager extends AbstractContextManager {

    private static ProgrammeContextManager instance = null;

    private ProgrammeContextManager() {
        instance = this;
    }

    @Override
    protected Context createContext() {
        return new ProgrammeUI();
    }

    @Override
    protected boolean isFullTextActive() {
        return false;
    }

    public static synchronized ProgrammeContextManager getInstance() {
        if (instance == null) {
            new ProgrammeContextManager();
        }

        return instance;
    }

    @Override
    protected URL getFilterConfigFileURL() {
        return Utils.getURL("file:data/filter/ProgrammeFilter.xml");
    }

    @Override
    protected String getDefaultQuery() {
        return "from " + Programme.class.getName();
    }

    @Override
    protected DataManager getDataManager() {
        return RepertoryDataManager.getInstance();
    }
}
