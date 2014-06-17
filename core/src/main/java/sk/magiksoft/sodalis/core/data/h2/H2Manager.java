
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/


package sk.magiksoft.sodalis.core.data.h2;

import org.h2.tools.Backup;
import org.h2.tools.Restore;
import org.hibernate.cfg.Configuration;
import sk.magiksoft.sodalis.core.data.DBManager;
import sk.magiksoft.sodalis.core.data.remote.DataManagerProvider;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.mapping.SodalisConfiguration;

import java.io.File;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jul 5, 2010
 * Time: 10:10:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class H2Manager implements DBManager {
    private static final URL FUNCTION_FILE_URL = H2Manager.class.getResource("functions.sql");
    private static final String DATABASE_DIR = "h2db";
    private static final String DATABASE_NAME = "sodalis";
    private static final String BACKUP_DATABASE_FILE = "data/temp/sodalis_bkp";

    private Configuration configuration;

    @Override
    public Configuration getConfiguration() {
        if (configuration == null) {
            configuration = new SodalisConfiguration().configure();
        }

        return configuration;
    }

    @Override
    public String getConnectionURL() {
        return "jdbc:h2:" + DATABASE_DIR + "/" + DATABASE_NAME /*+ ";FILE_LOCK=NO"*/;
    }

    @Override
    public String getDriverClassName() {
        return "org.h2.Driver";
    }

    @Override
    public String getDialect() {
        return "org.hibernate.dialect.H2Dialect";
    }

    @Override
    public boolean doBackup(File backupFile) throws Exception {
        try {
            if (backupFile.exists()) {
                backupFile.delete();
            }

            DataManagerProvider.getDataManager().closeSessionFactory();
            Backup.execute(backupFile.getAbsolutePath(), DATABASE_DIR, DATABASE_NAME, false);
        } finally {
            DataManagerProvider.getDataManager().resetSessionFactory();
        }
        return true;
    }

    @Override
    public boolean restore(File backupFile) throws Exception {
        try {
            DataManagerProvider.getDataManager().closeSessionFactory();
            Restore.execute(backupFile.getAbsolutePath(), DATABASE_DIR, DATABASE_NAME);
        } finally {
            DataManagerProvider.getDataManager().resetSessionFactory();
        }

        return true;
    }

    @Override
    public boolean backupDatabase(String dbName) {
        File file = new File(BACKUP_DATABASE_FILE);

        file.getParentFile().mkdirs();
        try {
            doBackup(file);
            return true;
        } catch (Exception e) {
            LoggerManager.getInstance().error(getClass(), e);
            return false;
        }
    }

    @Override
    public boolean restoreDatabase(String dbName) {
        File file = new File(BACKUP_DATABASE_FILE);

        if (!file.exists()) {
            return false;
        }
        try {
            restore(file);
            return true;
        } catch (Exception e) {
            LoggerManager.getInstance().error(getClass(), e);
            return false;
        }
    }

    @Override
    public URL getFunctionsURL() {
        return FUNCTION_FILE_URL;
    }
}