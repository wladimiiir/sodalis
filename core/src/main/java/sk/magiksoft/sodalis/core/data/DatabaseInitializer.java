
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.data;

import sk.magiksoft.sodalis.core.data.postgresql.PostgreSQLManager;
import sk.magiksoft.sodalis.core.data.remote.DataManagerProvider;
import sk.magiksoft.sodalis.core.data.remote.server.DataManager;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.enumeration.EnumerationFactory;
import sk.magiksoft.sodalis.core.imex.ImExManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.utils.FileUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class DatabaseInitializer {
    private static final String IMPORT_DIR = "data/import";
    private static DatabaseInitializer instance;

    public static void main(String[] args) {
        System.setProperty("installation", "TRUE");
        new DatabaseInitializer().initialize();
        System.exit(0);
    }

    private DatabaseInitializer() {
    }

    public static DatabaseInitializer getInstance() {
        if (instance == null) {
            instance = new DatabaseInitializer();
        }
        return instance;
    }

    private void createFunctions() {
        if (DBManagerProvider.getDBManager().getFunctionsURL() == null) {
            return;
        }
        final DataManager dataManager = DataManagerProvider.getDataManager();

        try {
            final String functionSQL = new String(FileUtils.getBytesFromStream(DBManagerProvider.getDBManager().getFunctionsURL().openStream()));
            dataManager.executeSQLQuery(functionSQL);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(getClass(), ex);
        } catch (IOException ex) {
            LoggerManager.getInstance().error(PostgreSQLManager.class, ex);
        }
    }

    public void initialize() {
        if (Boolean.valueOf(System.getProperty("useCreate", "false"))) {
            SchemaCreator.createDBSchema();
        } else {
            SchemaCreator.updateDBSchema();
        }
        createFunctions();
        EnumerationFactory.getInstance().importEnumerations(EnumerationFactory.ENUMERATION_FILE_URL);
        importFiles();
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
            for (Object imported : ImExManager.importFile(file)) {
                if(imported instanceof DatabaseEntity){
                    entities.add((DatabaseEntity) imported);
                }
            }
        }
        DefaultDataManager.getInstance().persistDatabaseEntities(entities);
    }
}