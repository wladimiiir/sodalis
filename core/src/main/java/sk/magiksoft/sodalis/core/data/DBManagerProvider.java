package sk.magiksoft.sodalis.core.data;

import sk.magiksoft.sodalis.core.PropertyHolder;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.logger.LoggerManager;

/**
 * @author wladimiiir
 * @since 2010/7/5
 */
public class DBManagerProvider {
    public static final String DB_MANAGER_CLASS_NAME = SodalisApplication.getProperty(PropertyHolder.DB_MANAGER_CLASS, "sk.magiksoft.sodalis.core.data.h2.H2Manager");

    private static DBManager dbManager;

    public static DBManager getDBManager() {
        if (dbManager == null) {
            createDBManager();
        }

        return dbManager;
    }

    private static void createDBManager() {
        try {
            dbManager = (DBManager) Class.forName(DB_MANAGER_CLASS_NAME).newInstance();
        } catch (ClassNotFoundException e) {
            LoggerManager.getInstance().error(DBManagerProvider.class, e);
        } catch (InstantiationException e) {
            LoggerManager.getInstance().error(DBManagerProvider.class, e);
        } catch (IllegalAccessException e) {
            LoggerManager.getInstance().error(DBManagerProvider.class, e);
        }
    }
}
