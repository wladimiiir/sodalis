
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.data.postgresql;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import sk.magiksoft.sodalis.core.PropertyHolder;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.data.DBConfiguration;
import sk.magiksoft.sodalis.core.data.DBManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.utils.FileUtils;
import sk.magiksoft.sodalis.core.utils.StreamUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

/**
 * @author wladimiiir
 */
public class PostgreSQLManager implements DBManager {

    private static final URL FUNCTION_FILE_URL = PostgreSQLManager.class.getResource("functions.sql");
    private static final String DATABASE_DIR = SodalisApplication.getProperty(PropertyHolder.DB_DIR, "./database");
    private static final String DATABASE_HOST = SodalisApplication.getProperty(PropertyHolder.DB_HOST, "localhost");
    private static final String DATABASE_NAME = SodalisApplication.getProperty(PropertyHolder.DB_NAME, "sodalis");
    private static final String DATABASE_USER = SodalisApplication.getProperty(PropertyHolder.DB_USER, "runtime");
    private static final String POSTGRES_BIN_DIR = SodalisApplication.getProperty(PropertyHolder.POSTGRES_BIN_DIR, "");
    private static final int CAUSE_UNKNOWN = 0;
    private static final int CAUSE_DB_ALREADY_RUNNING = 1;
    private int port = Integer.valueOf(SodalisApplication.getProperty(PropertyHolder.DB_PORT, "5432"));
    private Process postgresProcess;
    private Configuration configuration;

    public PostgreSQLManager() {
        runDB();
    }

    public Configuration getConfiguration() {
        if (configuration == null) {
            initHibernateConfiguration();
        }

        return configuration;
    }

    private int getCause(String error) {
        if (error.contains("postmaster.pid")) {
            return CAUSE_DB_ALREADY_RUNNING;
        }
        return CAUSE_UNKNOWN;
    }

    private int getRunningDBPort() throws IOException {
        final BufferedReader reader;
        String line;
        String portNumber = "";
        Integer runningPort = null;

        reader = new BufferedReader(new FileReader(new File(DATABASE_DIR, "postmaster.opts")));
        while ((line = reader.readLine()) != null) {
            //found port definition
            if (line.indexOf("-p") > 0) {
                line = line.substring(line.indexOf("-p") + 3).trim();

                for (int i = 0; i < line.length(); i++) {
                    if (!portNumber.isEmpty() && !Character.isDigit(line.charAt(i))) {
                        break;
                    } else if (Character.isDigit(line.charAt(i))) {
                        portNumber += line.charAt(i);
                    }
                }
                try {
                    runningPort = Integer.valueOf(portNumber);
                    break;
                } catch (NumberFormatException e) {
                }
            }
        }
        reader.close();

        if (runningPort == null) {
            throw new IOException();
        }
        return runningPort;
    }

    private void initHibernateConfiguration() throws HibernateException {
        configuration = new DBConfiguration().configure();
    }

    private static ProcessBuilder getProcessBuilder(boolean postgresUser, String... commands) {
        ProcessBuilder processBuilder = new ProcessBuilder(commands);

        if (postgresUser) {
            processBuilder.environment().put("PGUSER", "postgres");
            processBuilder.environment().put("PGPASSWORD", "postgres");
        } else {
            processBuilder.environment().put("PGUSER", DATABASE_USER);
            processBuilder.environment().put("PGPASSWORD", "HuD1nic0mr4D");
        }

        return processBuilder;
    }

    public void recreateDB() {
        try {
            System.out.println("Dropping database...");
            Process p = getProcessBuilder(false, POSTGRES_BIN_DIR + "dropdb", "-p", Integer.valueOf(port).toString(), DATABASE_NAME).start();
            System.out.println(StreamUtils.getInputStreamString(p.getInputStream()));
            p.waitFor();
        } catch (IOException ex) {
            LoggerManager.getInstance().error(PostgreSQLManager.class, ex);
        } catch (InterruptedException ex) {
            LoggerManager.getInstance().error(PostgreSQLManager.class, ex);
        }
        try {
            System.out.println("Creating database...");
            Process p = getProcessBuilder(false, POSTGRES_BIN_DIR + "createdb", "-p", Integer.valueOf(port).toString(), DATABASE_NAME).start();
            System.out.println(StreamUtils.getInputStreamString(p.getInputStream()));
            p.waitFor();
        } catch (IOException ex) {
            LoggerManager.getInstance().error(PostgreSQLManager.class, ex);
        } catch (InterruptedException ex) {
            LoggerManager.getInstance().error(PostgreSQLManager.class, ex);
        }
    }

    public void createUser() {
        try {
            System.out.println("Creating user...");
            Process p = getProcessBuilder(true, POSTGRES_BIN_DIR + "psql", "-p", String.valueOf(port), "-c",
                    "\"create user " + DATABASE_USER + " with password 'HuD1nic0mr4D'\"").start();
            System.out.println(StreamUtils.getInputStreamString(p.getInputStream()));
            p.waitFor();
        } catch (IOException ex) {
            LoggerManager.getInstance().error(PostgreSQLManager.class, ex);
        } catch (InterruptedException ex) {
            LoggerManager.getInstance().error(PostgreSQLManager.class, ex);
        }
    }

    public String getConnectionURL() {
        return "jdbc:postgresql://" + DATABASE_HOST + ":" + port + "/" + DATABASE_NAME;
    }

    @Override
    public String getDriverClassName() {
        return "org.postgresql.Driver";
    }

    @Override
    public String getDialect() {
        return "org.hibernate.dialect.PostgreSQLDialect";
    }

    private void runDB() {
        int exitValue = -1;
        ProcessBuilder processBuilder;

        if (postgresProcess != null) {
            return;
        }
        port--;
        while (exitValue != 0) {
            try {
                processBuilder = getProcessBuilder(false, POSTGRES_BIN_DIR + "pg_ctl", "start", "-D",
                        new File(DATABASE_DIR).getAbsolutePath());
                processBuilder.environment().put("PGPORT", String.valueOf(++port));
                postgresProcess = processBuilder.start();
                Thread.sleep(500);
                exitValue = postgresProcess.exitValue();
                if (exitValue != 0) {
                    String error = StreamUtils.getInputStreamString(postgresProcess.getErrorStream());
                    int cause = getCause(error);

                    switch (cause) {
                        case CAUSE_DB_ALREADY_RUNNING: {
                            port = getRunningDBPort();
                            exitValue = 0;
                            break;
                        }
                        default:
                            throw new RuntimeException(error);
                    }

                }
            } catch (InterruptedException ex) {
                LoggerManager.getInstance().error(PostgreSQLManager.class, ex);
            } catch (IllegalThreadStateException ex) {
                exitValue = 0;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void stopDB() {
        if (postgresProcess == null) {
            return;
        }
        postgresProcess.destroy();
        postgresProcess = null;
    }

    private static void prepareDBDir() {
        File dbDir = new File(DATABASE_DIR);

        if (dbDir.exists()) {
            FileUtils.deleteDir(dbDir);
        }
        dbDir.mkdirs();
    }

    public static void initDB() {
        prepareDBDir();
        try {
            Process p = getProcessBuilder(true, POSTGRES_BIN_DIR + "initdb",
                    "-E", "UTF-8", "-U", DATABASE_USER,
                    "-D", new File(DATABASE_DIR).getAbsolutePath()).start();
            System.out.println("Initializing database space...");
            System.out.println(StreamUtils.getInputStreamString(p.getInputStream()));
            p.waitFor();
        } catch (IOException ex) {
            LoggerManager.getInstance().error(PostgreSQLManager.class, ex);
        } catch (InterruptedException ex) {
            LoggerManager.getInstance().error(PostgreSQLManager.class, ex);
        }
    }

    public boolean backupDatabase(String dbName) {
        try {
            Process p = new ProcessBuilder(POSTGRES_BIN_DIR + "dropdb", "-p", String.valueOf(port), dbName + "_bkp").start();
            p.waitFor();
            p = new ProcessBuilder(POSTGRES_BIN_DIR + "createdb", "-p", String.valueOf(port), dbName + "_bkp", "-T", dbName).start();
            return p.waitFor() == 0;
        } catch (InterruptedException ex) {
            LoggerManager.getInstance().error(PostgreSQLManager.class, ex);
        } catch (IOException ex) {
            LoggerManager.getInstance().error(PostgreSQLManager.class, ex);
        }
        return false;
    }

    public boolean restoreDatabase(String dbName) {
        try {
            Process p = new ProcessBuilder(POSTGRES_BIN_DIR + "dropdb", "-p", String.valueOf(port), dbName).start();
            p.waitFor();
            p = new ProcessBuilder(POSTGRES_BIN_DIR + "psql", "-p", String.valueOf(port), "postgres", "-c", "ALTER DATABASE " + dbName + "_bkp RENAME TO " + dbName).start();
            return p.waitFor() == 0;
        } catch (InterruptedException ex) {
            LoggerManager.getInstance().error(PostgreSQLManager.class, ex);
        } catch (IOException ex) {
            LoggerManager.getInstance().error(PostgreSQLManager.class, ex);
        }
        return false;

    }

    @Override
    public URL getFunctionsURL() {
        return FUNCTION_FILE_URL;
    }

    public boolean doBackup(File backupFile) throws IOException, InterruptedException {
        Process process = new ProcessBuilder(POSTGRES_BIN_DIR + "pg_dump",
                "-f", backupFile.getAbsolutePath(), "-p", String.valueOf(port),
                "-U", DATABASE_USER, DATABASE_NAME, "-F", "c").start();
        int result = process.waitFor();

        if (result != 0) {
            LoggerManager.getInstance().error(PostgreSQLManager.class,
                    new Exception(StreamUtils.getInputStreamString(process.getErrorStream())));
            return false;
        }

        return true;
    }

    public boolean restore(File backupFile) throws IOException, InterruptedException {
        Process process = new ProcessBuilder(POSTGRES_BIN_DIR + "pg_restore",
                "-c", "-p", String.valueOf(port), "-U", DATABASE_USER,
                "-d", DATABASE_NAME, backupFile.getAbsolutePath()).start();
        int result = process.waitFor();

        if (result != 0) {
            LoggerManager.getInstance().error(PostgreSQLManager.class,
                    new Exception(StreamUtils.getInputStreamString(process.getErrorStream())));
            return false;
        }

        return true;
    }
}