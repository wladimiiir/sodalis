package sk.magiksoft.sodalis.core.data;

import sk.magiksoft.sodalis.core.data.remote.DataManagerProvider;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.enumeration.EnumerationFactory;
import sk.magiksoft.sodalis.core.imex.ImExManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.utils.FileUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class DatabaseInitializer {
    private static final String IMPORT_DIR = "data/import";
    private static DatabaseInitializer instance;

    private DatabaseInitializer() {
    }

    public static DatabaseInitializer getInstance() {
        if (instance == null) {
            instance = new DatabaseInitializer();
        }
        return instance;
    }

    public void initialize() {
        if (Boolean.valueOf(System.getProperty("db.init.useUpdateMethod", "false"))) {
            SchemaCreator.updateDBSchema();
        } else {
            SchemaCreator.createDBSchema();
        }

        createFunctions();
        importEnumerations();
        importFiles();
    }

    private void createFunctions() {
        if (DBManagerProvider.getDBManager().getFunctionsURL() == null) {
            return;
        }

        try {
            final String functionSQL = new String(FileUtils.getBytesFromStream(DBManagerProvider.getDBManager().getFunctionsURL().openStream()));
            DataManagerProvider.getDataManager().executeSQLQuery(functionSQL);
        } catch (IOException ex) {
            LoggerManager.getInstance().error(getClass(), ex);
        }
    }

    private void importEnumerations() {
        EnumerationFactory.getInstance().importEnumerations(EnumerationFactory.ENUMERATION_FILE_URL);
    }

    private void importFiles() {
        final File[] files = new File(IMPORT_DIR).listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".xml");
            }
        });

        final List<DatabaseEntity> entities = new LinkedList<>();
        for (File file : files) {
            try {
                for (Object imported : ImExManager.importFile(file)) {
                    if (imported instanceof DatabaseEntity
                            && ((DatabaseEntity) imported).getId() == null) {
                        entities.add((DatabaseEntity) imported);
                    }
                }
            } catch (Exception e) {
                LoggerManager.getInstance().warn(getClass(), e);
            }
        }
        DefaultDataManager.getInstance().persistDatabaseEntities(entities);
    }
}
