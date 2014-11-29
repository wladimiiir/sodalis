package sk.magiksoft.sodalis.folkensemble.repertory;

import sk.magiksoft.sodalis.core.context.AbstractContextManager;
import sk.magiksoft.sodalis.core.context.Context;
import sk.magiksoft.sodalis.core.data.remote.server.DataManager;
import sk.magiksoft.sodalis.core.utils.Utils;
import sk.magiksoft.sodalis.folkensemble.repertory.data.RepertoryDataManager;
import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song;
import sk.magiksoft.sodalis.folkensemble.repertory.ui.RepertoryUI;

import java.net.URL;

/**
 * @author wladimiiir
 */
public class RepertoryContextManager extends AbstractContextManager {

    private static RepertoryContextManager instance = null;

    private RepertoryContextManager() {
        instance = this;
    }

    @Override
    protected Context createContext() {
        return new RepertoryUI();
    }

    @Override
    protected boolean isFullTextActive() {
        return false;
    }

    public static synchronized RepertoryContextManager getInstance() {
        if (instance == null) {
            new RepertoryContextManager();
        }

        return instance;
    }

    @Override
    protected URL getFilterConfigFileURL() {
        return Utils.getURL("file:data/filter/RepertoryFilter.xml");
    }

    @Override
    protected String getDefaultQuery() {
        return "from " + Song.class.getName();
    }

    @Override
    protected DataManager getDataManager() {
        return RepertoryDataManager.getInstance();
    }
}
