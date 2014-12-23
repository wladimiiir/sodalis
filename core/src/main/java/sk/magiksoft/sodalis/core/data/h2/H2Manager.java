package sk.magiksoft.sodalis.core.data.h2;

import org.h2.tools.Backup;
import org.h2.tools.Restore;
import sk.magiksoft.sodalis.core.data.DBConfiguration;
import sk.magiksoft.sodalis.core.data.DBManager;
import sk.magiksoft.sodalis.core.data.remote.DataManagerProvider;
import sk.magiksoft.sodalis.core.logger.LoggerManager;

import java.io.File;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author wladimiiir
 * @since 2010/7/5
 */
public class H2Manager implements DBManager {
    private static final URL FUNCTION_FILE_URL = H2Manager.class.getResource("functions.sql");
    private static final String DATABASE_DIR = "./database/h2db";
    private static final String DATABASE_NAME = "sodalis";
    private static final String BACKUP_DATABASE_FILE = "data/temp/sodalis_bkp";

    private DBConfiguration dbConfiguration;

    @Override
    public DBConfiguration getConfiguration() {
        if (dbConfiguration == null) {
            dbConfiguration = new DBConfiguration();
        }

        return dbConfiguration;
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
    public boolean isDBPresent() {
        Connection connection = null;
        final PreparedStatement statement;
        final boolean result;
        try {
            connection = DriverManager.getConnection(getConnectionURL(), getConfiguration().getUsername(), getConfiguration().getPassword());
            statement = connection.prepareStatement("SELECT count(*) FROM person");
            result = statement.execute();
            statement.close();
            return result;
        } catch (SQLException e) {
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    return false;
                }
            }
        }
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

    @Override
    public boolean resetSessionFactory() {
        try {
            DataManagerProvider.getDataManager().resetSessionFactory();
            return true;
        } catch (RemoteException e) {
            LoggerManager.getInstance().error(getClass(), e);
            return false;
        }
    }
}
