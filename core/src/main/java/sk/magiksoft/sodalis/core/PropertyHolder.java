
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import sk.magiksoft.sodalis.core.logger.LoggerManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    public PropertyHolder(File propertyFile, boolean xmlFile) {
        if (propertyFile.exists()) {

            if (xmlFile) {
                try {
                    Document xmlDocument = new SAXBuilder().build(propertyFile);
                    Element properties = xmlDocument.getRootElement().getChild("properties");

                    for (int i = 0; i < properties.getChildren().size(); i++) {
                        Element property = (Element) properties.getChildren().get(i);
                        put(property.getAttributeValue("key"), property.getAttributeValue("value"));
                    }

                } catch (JDOMException ex) {
                    LoggerManager.getInstance().error(PropertyHolder.class, ex);
                } catch (IOException ex) {
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
                    load(new FileReader(propertyFile));
                } catch (IOException ex) {
                    LoggerManager.getInstance().error(getClass(), ex);
                }
            }

        }
    }
}