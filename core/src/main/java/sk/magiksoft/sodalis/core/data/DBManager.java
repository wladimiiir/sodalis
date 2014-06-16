
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/


package sk.magiksoft.sodalis.core.data;

import org.hibernate.cfg.Configuration;

import java.io.File;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jul 5, 2010
 * Time: 9:37:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DBManager {
    Configuration getConfiguration();

    String getConnectionURL();

    String getDriverClassName();

    String getDialect();

    boolean doBackup(File backupFile) throws Exception;

    boolean restore(File backupFile) throws Exception;

    boolean backupDatabase(String dbName);

    boolean restoreDatabase(String dbName);

    URL getFunctionsURL();
}