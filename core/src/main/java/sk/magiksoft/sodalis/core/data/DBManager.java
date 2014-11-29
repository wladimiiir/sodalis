package sk.magiksoft.sodalis.core.data;

import org.hibernate.cfg.Configuration;

import java.io.File;
import java.net.URL;

/**
 * @author wladimiiir
 * @since 2010/7/5
 */
public interface DBManager {
    Configuration getConfiguration();

    String getConnectionURL();

    String getDriverClassName();

    String getDialect();

    boolean isDBPresent();

    boolean doBackup(File backupFile) throws Exception;

    boolean restore(File backupFile) throws Exception;

    boolean backupDatabase(String dbName);

    boolean restoreDatabase(String dbName);

    URL getFunctionsURL();
}
