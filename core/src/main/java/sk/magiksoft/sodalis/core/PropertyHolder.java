package sk.magiksoft.sodalis.core;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import sk.magiksoft.sodalis.core.logger.LoggerManager;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * @author wladimiiir
 */
public class PropertyHolder extends Properties {

    public static final String MODULE_JAR = "module.jar";
    public static final String JAVA_BIN = "java.bin";
    public static final String LIB_DIR = "lib.dir";
    public static final String UPDATE_WEBSERVICE_LOCATION = "update.webservice.location";
    public static final String UPDATE_WEBSERVICE_NAMESPACE = "update.webservice.namespace";
    public static final String UPDATE_WEBSERVICE_NAME = "update.webservice.name";
    public static final String UPDATE_WEBSERVICE_URL_BASE = "update.webservice.url.base";
    public static final String UPDATE_WEBSERVICE_URL_MAINJAR = "update.webservice.url.mainjar";
    public static final String UPDATE_SODALIS_PATH = "update.sodalis.path";
    public static final String DB_HOST = "db.host";
    public static final String DB_PORT = "db.port";
    public static final String DB_DIR = "db.dir";
    public static final String DB_MANAGER_CLASS = "db.manager.class";
    public static final String DB_FUNCTIONS_SQL = "db.functions.sql";
    public static final String DB_NAME = "db.name";
    public static final String DB_USER = "db.user";
    public static final String POSTGRES_BIN_DIR = "postgres.bin";
    public static final String SERVER_LOCATION = "server.location";
    public static final String CLIENT_LOCATION = "client.location";
    public static final String LOCALE_COUNTRY = "locale.country";
    public static final String LOCALE_LANGUAGE = "locale.language";
    public static final String RESTORE_DATABASE = "restoreDB";

    public PropertyHolder(URL propertyFileURL, boolean xmlFile) {
        if (xmlFile) {
            try {
                final Document xmlDocument = new SAXBuilder().build(propertyFileURL);
                final Element properties = xmlDocument.getRootElement().getChild("properties");

                for (int i = 0; i < properties.getChildren().size(); i++) {
                    final Element property = properties.getChildren().get(i);
                    put(property.getAttributeValue("key"), property.getAttributeValue("value"));
                }

            } catch (JDOMException | IOException ex) {
                LoggerManager.getInstance().error(PropertyHolder.class, ex);
            }
            try {
                final FileWriter fileWriter = new FileWriter("data/sodalis.properties");
                store(fileWriter, "");
                fileWriter.close();
            } catch (IOException ex) {
                LoggerManager.getInstance().error(getClass(), ex);
            }
        } else {
            try {
                load(propertyFileURL.openStream());
            } catch (IOException ex) {
                LoggerManager.getInstance().error(getClass(), ex);
            }
        }
    }
}
