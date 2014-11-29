package sk.magiksoft.sodalis.core.security;

import sk.magiksoft.sodalis.core.data.remote.client.ClientDataManager;
import sk.magiksoft.sodalis.core.security.entity.SodalisUser;

/**
 * @author wladimiiir
 */
public class SecurityDataManager extends ClientDataManager {
    private static SecurityDataManager instance;

    protected SecurityDataManager() {
    }

    public static SecurityDataManager getInstance() {
        if (instance == null) {
            instance = new SecurityDataManager();
        }

        return instance;
    }

    public boolean isAdminPresent() {
        return !getDatabaseEntities(SodalisUser.class, "admin=true").isEmpty();
    }
}
