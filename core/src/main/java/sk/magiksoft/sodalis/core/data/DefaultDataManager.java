package sk.magiksoft.sodalis.core.data;

import sk.magiksoft.sodalis.core.data.remote.client.ClientDataManager;

/**
 * @author wladimiiir
 */
public class DefaultDataManager extends ClientDataManager {
    private static DefaultDataManager instance;

    private DefaultDataManager() {
    }

    public static DefaultDataManager getInstance() {
        if (instance == null) {
            instance = new DefaultDataManager();
        }
        return instance;
    }
}
