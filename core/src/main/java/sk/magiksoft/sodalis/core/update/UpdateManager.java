
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.update;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import sk.magiksoft.sodalis.core.PropertyHolder;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.data.DatabaseUpdater;
import sk.magiksoft.sodalis.core.data.remote.DataManagerProvider;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;
import sk.magiksoft.sodalis.core.update.updater.UpdaterManager;
import sk.magiksoft.sodalis.core.utils.FileUtils;
import sk.magiksoft.sodalis.core.utils.ProcessUtils;
import sk.magiksoft.sodalis.core.utils.Utils;
import sk.magiksoft.sodalis.core.utils.WebUtils;
import sk.magiksoft.swing.ProgressDialog;
import sk.magiksoft.sodalis.updater.*;

import javax.swing.*;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;
import java.io.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author wladimiiir
 * @since
 */
public class UpdateManager {
    private static final FilenameFilter JAR_FILE_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".jar");
        }
    };
    //result info codes
    private static final String RESULT_UPDATE_COMPLETED = "updateCompleted";
    private static final String RESULT_NO_UPDATE = "noUpdateAvailable";
    private static final String RESULT_CONNECTION_ERROR = "updateConnectionError";
    private static final String RESULT_WRONG_UPDATE_FILE = "wrongUpdateFile";
    private static final String RESULT_PROCESSING_UPDATERS_ERROR = "processingUpdatersError";
    private static final String RESULT_UNKNOWN_ERROR = "updateUnknownError";
    private static final String RESULT_NO_MESSAGE = "noMessage";
    private static final String RESULT_DOWNLOAD_ERROR = "updateDownloadError";
    private static final String RESULT_BACKUP_FAILED = "backupFailed";
    private static final String RESULT_SCHEMA_UPDATE_ERROR = "schemaUpdateError";
    private static final String RESULT_DB_RESTORE_ERROR = "dbRestoreError";
    private static final String RESULT_COPY_FILE_ERROR = "copyFileError";
    //
    private static UpdateManager instance = null;
    private static final String[] UPDATE_DIRS = {
            "lib", "data"
    };
    private static final String UPDATERS_DIR = "updaters";
    private static final String UPDATE_TEMP_DIR = "data" + File.separator + "temp" + File.separator + "update";
    private static final String UPDATE_FILENAME = UPDATE_TEMP_DIR + File.separator + "update.zip";
    private static final Comparator<Element> UPDATE_COMPARATOR = new Comparator<Element>() {

        @Override
        public int compare(Element o1, Element o2) {
            String v1 = o1.getAttributeValue("version");
            String v2 = o2.getAttributeValue("version");

            v1 = v1.substring(v1.lastIndexOf(".") + 1);
            v2 = v2.substring(v2.lastIndexOf(".") + 1);

            return Integer.valueOf(v2) - Integer.valueOf(v1);
        }
    };
    private ProgressDialog dialog;


    public UpdateManager() {
        instance = this;
    }

    public static UpdateManager getInstance() {
        if (instance == null) {
            new UpdateManager();
        }
        return instance;
    }

    public void generateUpdate(File updateFile, File diffFile, String version, String moduleJar) {
        Element updateElement;
        FileWriter writer;
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        Document updateDocument, differenceDocument;

        try {
            if (!updateFile.exists()) {
                updateDocument = new Document(new Element("updates"));
            } else {
                updateDocument = new SAXBuilder().build(updateFile);
            }
            differenceDocument = new SAXBuilder().build(diffFile);

            updateElement = generateUpdateElement(differenceDocument, version, moduleJar);
            updateDocument.getRootElement().getChildren().add(updateElement);

            writer = new FileWriter(updateFile);
            outputter.output(updateDocument, writer);
            writer.close();
        } catch (JDOMException ex) {
            LoggerManager.getInstance().error(UpdateManager.class, ex);
        } catch (IOException ex) {
            LoggerManager.getInstance().error(UpdateManager.class, ex);
        }
    }

    private Element generateUpdateElement(Document differenceDocument, String version, String moduleJar) {
        Element updateElement = new Element("update");
        List<Element> diffEntries = differenceDocument.getRootElement().getChildren();
        Element updateFile;
        Element entryFileName;

        updateElement.setAttribute("version", version);

        for (Element entry : diffEntries) {
            if (entry.getChild("file") == null || entry.getChild("file").getChild("revision") == null || entry.getChild("file").getChild("name") == null) {
                continue;
            }
            entryFileName = entry.getChild("file").getChild("name");
            for (int i = 0; i < UPDATE_DIRS.length; i++) {
                String updateDir = UPDATE_DIRS[i];
                if (entryFileName.getTextTrim().startsWith(updateDir + "/")) {
                    updateFile = new Element("file");
                    updateFile.setText(entryFileName.getTextTrim());
                    updateElement.getChildren().add(updateFile);
                    break;
                }
            }
            if (entryFileName.getTextTrim().startsWith(UPDATERS_DIR + "/")
                    && entryFileName.getTextTrim().endsWith(".class")
                    && entry.getChild("file").getChild("prerevision") == null) {
                updateFile = new Element("updater");
                updateFile.setText(entryFileName.getTextTrim().substring(UPDATERS_DIR.length() + 1));
                updateElement.getChildren().add(updateFile);
            }
        }

        updateFile = new Element("file");
        updateFile.setText("lib/" + moduleJar);
        updateElement.getChildren().add(updateFile);

        return updateElement;
    }

    public void generateUpdateZipFile(File[] updatesFiles, String[] forVersions, File updateZipFile) {
        Set<File> files = new HashSet<File>();
        ZipEntry zipEntry;
        ZipOutputStream zos;
        FileInputStream fis;
        Document updateDocument;

        try {
            zos = new ZipOutputStream(new FileOutputStream(updateZipFile));
            for (int i = 0; i < updatesFiles.length; i++) {
                File updatesFile = updatesFiles[i];
                String forVersion = forVersions[i];

                updateDocument = new SAXBuilder().build(updatesFile);
                files.addAll(getUpdateFilesForVersion(updateDocument, forVersion));
            }

            for (File file : files) {
                zipEntry = new ZipEntry(file.getPath());
                zos.putNextEntry(zipEntry);
                fis = new FileInputStream(file);
                while (fis.available() > 0) {
                    zos.write(fis.read());
                }
                fis.close();
            }

            zos.close();
        } catch (JDOMException ex) {
            LoggerManager.getInstance().error(UpdateManager.class, ex);
        } catch (IOException ex) {
            LoggerManager.getInstance().error(UpdateManager.class, ex);
        }
    }

    public void generateUpdateZipFile(File updatesFile, String forVersion, File updateZipFile) {
        List<File> files;
        ZipEntry zipEntry;
        ZipOutputStream zos;
        FileInputStream fis;
        Document updateDocument;

        try {
            updateDocument = new SAXBuilder().build(updatesFile);
            files = getUpdateFilesForVersion(updateDocument, forVersion);
            zos = new ZipOutputStream(new FileOutputStream(updateZipFile));

            for (File file : files) {
                zipEntry = new ZipEntry(file.getPath());
                zos.putNextEntry(zipEntry);
                fis = new FileInputStream(file);
                while (fis.available() > 0) {
                    zos.write(fis.read());
                }
                fis.close();
            }

            zos.close();
        } catch (JDOMException ex) {
            LoggerManager.getInstance().error(UpdateManager.class, ex);
        } catch (IOException ex) {
            LoggerManager.getInstance().error(UpdateManager.class, ex);
        }
    }

    private List<File> getUpdateFilesForVersion(Document updateDocument, String version) {
        Set<File> files = new HashSet<File>();
        List<Element> updates = updateDocument.getRootElement().getChildren();

        Collections.sort(updates, Collections.reverseOrder(UPDATE_COMPARATOR));

        for (Element update : updates) {
            if (update.getAttributeValue("version").equals(version)) {
                break;
            }
            for (int i = 0; i < update.getChildren().size(); i++) {
                Element fileElement = update.getChildren().get(i);
                files.add(new File(fileElement.getTextTrim()));
            }
        }

        return new ArrayList<>(files);
    }

    public void doUpdate(final File updateFile) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                String result = RESULT_NO_MESSAGE;

                dialog = new ProgressDialog();
                dialog.setProgressMessage(LocaleManager.getString("gettingUpdateInfo"));
                dialog.startProgress();

                try {
                    updateFromFile(updateFile, false);
                } finally {
                    if ((result.equals(RESULT_NO_UPDATE) || result.equals(RESULT_UPDATE_COMPLETED)) && isUpdatersAvailable()) {
                        result = proceedUpdaters();
                        if (result.equals(RESULT_NO_MESSAGE)) {
                            result = RESULT_UPDATE_COMPLETED;
                        }
                    }
                    dialog.stopProgress();
                    showResultInfo(result);
                    clearUpdateTemp();
                    if (result.equals(RESULT_UPDATE_COMPLETED)) {
                        SodalisApplication.get().restartApplication();
                    }
                }
            }
        }).start();
    }

    public void doUpdate() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                String result = RESULT_NO_MESSAGE;

                dialog = new ProgressDialog();
                dialog.setProgressMessage(LocaleManager.getString("gettingUpdateInfo"));
                dialog.startProgress();

                try {
                    final QName qName = new QName(SodalisApplication.getProperty(PropertyHolder.UPDATE_WEBSERVICE_NAMESPACE, null),
                            SodalisApplication.getProperty(PropertyHolder.UPDATE_WEBSERVICE_NAME, "UpdateService"));
                    final UpdateServiceService service = new UpdateServiceService(new URL(SodalisApplication.getProperty(PropertyHolder.UPDATE_WEBSERVICE_LOCATION,
                            "http://www.sodalis.sk/update.php?wsdl")), qName);
                    final UpdateService updateService = service.getUpdateServicePort();
                    final Map<String, String> propertyMap = createUpdatePropertyMap();
                    final String updateFile = updateService.createUpdate(createRequestType(propertyMap));

                    if (updateFile == null) {
                        result = RESULT_NO_UPDATE;
                        return;
                    }

                    if (ISOptionPane.showConfirmDialog(null, LocaleManager.getString("updateConfirm"),
                            LocaleManager.getString("update"), JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                        result = RESULT_NO_MESSAGE;
                        return;
                    }
                    // downloading files
                    if (!(result = downloadUpdateFiles(updateFile)).equals(RESULT_NO_MESSAGE)) {
                        return;
                    }

                    result = updateFromFile(new File(UPDATE_FILENAME), true);
                } catch (WebServiceException e) {
                    LoggerManager.getInstance().info(getClass(), e);
                    result = RESULT_CONNECTION_ERROR;
                } catch (MalformedURLException e) {
                    LoggerManager.getInstance().info(getClass(), e);
                    result = RESULT_CONNECTION_ERROR;
                } catch (IOException e) {
                    LoggerManager.getInstance().info(getClass(), e);
                    result = RESULT_CONNECTION_ERROR;
                } catch (Exception e) {
                    LoggerManager.getInstance().info(getClass(), e);
                    result = RESULT_UNKNOWN_ERROR;
                } finally {
                    if ((result.equals(RESULT_NO_UPDATE) || result.equals(RESULT_UPDATE_COMPLETED)) && isUpdatersAvailable()) {
                        result = proceedUpdaters();
                        if (result.equals(RESULT_NO_MESSAGE)) {
                            result = RESULT_UPDATE_COMPLETED;
                        }
                    }
                    dialog.stopProgress();
                    showResultInfo(result);
                    clearUpdateTemp();
//                    DataManagerAdater.getSessionFactory().openSession();
                    if (result.equals(RESULT_UPDATE_COMPLETED)) {
                        SodalisApplication.get().restartApplication();
                    }
                }
            }

            private CreateUpdate.Properties createRequestType(Map<String, String> propertyMap) {
                final ObjectFactory objectFactory = new ObjectFactory();
                final CreateUpdate.Properties properties = objectFactory.createCreateUpdateProperties();
                for (Map.Entry<String, String> mapEntry : propertyMap.entrySet()) {
                    final CreateUpdate.Properties.Entry entry = new CreateUpdate.Properties.Entry();
                    entry.setKey(mapEntry.getKey());
                    entry.setValue(mapEntry.getValue());
                    properties.getEntry().add(entry);
                }
                return properties;
            }
        }).start();
    }

    private Map<String, String> createUpdatePropertyMap() {
        Map<String, String> map = new HashMap<String, String>();
        File[] moduleJarFiles = Utils.getModuleJarFiles();
        for (File moduleJarFile : moduleJarFiles) {
            String module = moduleJarFile.getName();
            module = module.substring("sodalis-".length(), module.lastIndexOf(".jar"));
            if (map.containsKey("modules")) {
                map.put("modules", map.get("modules") + ";" + module);
            } else {
                map.put("modules", module);
            }
            try {
                JarFile jarFile = new JarFile(moduleJarFile);
                map.put(module + ".version", jarFile.getManifest().getMainAttributes().getValue("Version").toString());
            } catch (IOException e) {
            }
        }

        map.put("sodalisPath", SodalisApplication.getProperty(PropertyHolder.UPDATE_SODALIS_PATH, "sodalis"));
        map.put("os", Utils.getOSName());
        try {
            map.put("clientIP", InetAddress.getLocalHost().toString());
        } catch (UnknownHostException e) {
        }

        return map;
    }


    private String updateFromFile(File updateFile, boolean deleteUpdateFile) {
        String result;

        //unpacking data and lib files
        if (!(result = unpackFiles(updateFile)).equals(RESULT_NO_MESSAGE)) {
            return result;
        }

        if (deleteUpdateFile) {
            updateFile.delete();
        }

        if (!(result = backUpDB()).equals(RESULT_NO_MESSAGE)) {
            return result;
        }

        deleteHibernateCache();
        if (!(result = updateDBSchema()).equals(RESULT_NO_MESSAGE)) {
            restoreDB();
            return result;
        }

        if (isUpdatersAvailable() && !(result = proceedUpdaters()).equals(RESULT_NO_MESSAGE)) {
            restoreDB();
            return result;
        }

        try {
            Class.forName(ProcessUtils.class.getName());
            Utils.getClassPath();
        } catch (ClassNotFoundException e) {
            LoggerManager.getInstance().error(getClass(), e);
        }
        if (!(result = copyTempFiles()).equals(RESULT_NO_MESSAGE)) {
            restoreDB();
            return result;
        }

        return RESULT_UPDATE_COMPLETED;
    }

    private void deleteHibernateCache() {
        final File cacheDir = new File("data/cache/");
        for (File file : cacheDir.listFiles()) {
            if (file.isFile()) {
                file.delete();
            }
        }
    }

    private void clearUpdateTemp() {
        FileUtils.deleteDir(new File(UPDATE_TEMP_DIR));
    }

    private boolean isUpdatersAvailable() {
        return UpdaterManager.getInstance().isUpdatersAvailable();
    }

    private String proceedUpdaters() {
        dialog.setProgressMessage(LocaleManager.getString("processingUpdaters"));
        try {
            UpdaterManager.getInstance().proceedUpdaters();
        } catch (Exception ex) {
            LoggerManager.getInstance().info(getClass(), ex);
            return RESULT_PROCESSING_UPDATERS_ERROR;
        }

        return RESULT_NO_MESSAGE;
    }

    private String downloadUpdateFiles(String updateFile) throws MalformedURLException, IOException {
        dialog.setProgressMessage(LocaleManager.getString("downloadingUpdateFile"));
        URL fileURL = new URL(SodalisApplication.getProperty(PropertyHolder.UPDATE_WEBSERVICE_URL_BASE, "") + updateFile);
        WebUtils.downloadFile(fileURL, new File(UPDATE_FILENAME));
//        fileURL = new URL(SodalisApplication.getProperty(PropertyHolder.UPDATE_WEBSERVICE_URL_MAINJAR, ""));
//        WebUtils.downloadFile(fileURL, new File(MAINJAR_FILE));

        return RESULT_NO_MESSAGE;
    }

    private String unpackFiles(File updateFile) {
        dialog.setProgressMessage(LocaleManager.getString("copyingFiles"));
        try {
            FileUtils.unpackZipFile(updateFile, new File(UPDATE_TEMP_DIR));
        } catch (IOException ex) {
            LoggerManager.getInstance().info(getClass(), ex);
            return RESULT_WRONG_UPDATE_FILE;
        }

        return RESULT_NO_MESSAGE;
    }

    private String backUpDB() {
        dialog.setProgressMessage(LocaleManager.getString("backingUpDB"));
        try {
//            DataManagerAdater.getCurrentSession().close();
//            DataManagerAdater.getSessionFactory().close();
            return SodalisApplication.getDBManager().backupDatabase("sodalis") ? RESULT_NO_MESSAGE : RESULT_BACKUP_FAILED;
        } finally {
//            DataManagerAdater.getSessionFactory().openSession();
        }
    }

    private String updateDBSchema() {
        dialog.setProgressMessage(LocaleManager.getString("updatingDBSchema"));
        try {
            DataManagerProvider.getDataManager().closeSessionFactory();
            if (ProcessUtils.runCommand(true, SodalisApplication.getProperty(PropertyHolder.JAVA_BIN, "./jre/bin/java"),
                    "-cp", getLibrariesPath(), DatabaseUpdater.class.getName()) != 0) {
                return RESULT_SCHEMA_UPDATE_ERROR;
            }
        } catch (Exception ex) {
            LoggerManager.getInstance().warn(getClass(), ex);
            return RESULT_SCHEMA_UPDATE_ERROR;
        } finally {
            try {
                DataManagerProvider.getDataManager().resetSessionFactory();
            } catch (RemoteException e) {
                LoggerManager.getInstance().warn(getClass(), e);
                return RESULT_SCHEMA_UPDATE_ERROR;
            }
        }
        return RESULT_NO_MESSAGE;
    }

    private String getLibrariesPath() {
        StringBuilder librariesPath = new StringBuilder();
        File libDir = new File(UPDATE_TEMP_DIR + File.separator + SodalisApplication.getProperty(PropertyHolder.LIB_DIR, "lib"));

        if (libDir.exists()) {
            for (File jarFile : libDir.listFiles(JAR_FILE_FILTER)) {
                if (librariesPath.length() > 0) {
                    librariesPath.append(File.pathSeparator);
                }
                librariesPath.append(UPDATE_TEMP_DIR).append(File.separator).append(libDir.getName()).append(File.separator).append(jarFile.getName());
            }
        }
        libDir = new File(SodalisApplication.getProperty(PropertyHolder.LIB_DIR, "lib"));
        for (File jarFile : libDir.listFiles(JAR_FILE_FILTER)) {
            if (librariesPath.indexOf(File.separator + jarFile.getName()) != -1) {
                continue;
            }
            if (librariesPath.length() > 0) {
                librariesPath.append(File.pathSeparator);
            }
            librariesPath.append(libDir.getName()).append(File.separator).append(jarFile.getName());
        }

        return librariesPath.toString();
    }

    private String copyTempFiles() {
        dialog.setProgressMessage(LocaleManager.getString("copyingFiles"));

        try {
            FileUtils.copyDirectory(new File(UPDATE_TEMP_DIR), new File("."), true);
            final File updatersFile = new File("updaters.jar");
            if (updatersFile.exists()) {
                updatersFile.delete();
            }
        } catch (IOException ex) {
            LoggerManager.getInstance().info(getClass(), ex);
            return RESULT_COPY_FILE_ERROR;
        }
        return RESULT_NO_MESSAGE;
    }

    private String restoreDB() {
        dialog.setProgressMessage(LocaleManager.getString("restoringDB"));
        return SodalisApplication.getDBManager().restoreDatabase("sodalis") ? RESULT_NO_MESSAGE : RESULT_DB_RESTORE_ERROR;
    }

    private void showResultInfo(String result) {
        if (result.equals(RESULT_NO_MESSAGE)) {
            return;
        }

        ISOptionPane.showMessageDialog(null, LocaleManager.getString(result));
    }
}