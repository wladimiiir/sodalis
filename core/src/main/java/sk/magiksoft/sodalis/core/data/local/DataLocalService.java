package sk.magiksoft.sodalis.core.data.local;

import sk.magiksoft.sodalis.core.data.DataService;
import sk.magiksoft.sodalis.core.data.remote.server.DataManager;

/**
 * @author wladimiiir
 * @since 2010/5/18
 */
public interface DataLocalService extends DataService {
    @Override
    DataManager getDataManager();
}
