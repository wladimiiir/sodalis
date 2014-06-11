
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.core.data;

import sk.magiksoft.sodalis.core.PropertyHolder;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.data.postgresql.PostgreSQLManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jul 5, 2010
 * Time: 9:40:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBManagerProvider {
    public static final String DB_MANAGER_CLASS_NAME = SodalisApplication.getProperty(PropertyHolder.DB_MANAGER_CLASS, PostgreSQLManager.class.getName());

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