
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/


package sk.magiksoft.sodalis.core.mapping;

import org.hibernate.cfg.Configuration;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.history.HistoryInterceptor;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.security.CryptoUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: May 18, 2010
 * Time: 7:01:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class SodalisConfiguration extends Configuration {
    private static final byte[] CONFIG = new byte[]{-118, -71, 97, 108, -41, 7, -91, -20, -104, -15, 86, 63, 18, 89, 24, 118};

    public SodalisConfiguration() {
        initProperties();
        initInterceptors();
        initMappings();
    }

    private void initMappings() {
        String[] mappings = new String[]{
                "sodalis.hbm.xml", "person.hbm.xml", "ensemble.hbm.xml", "inventory.hbm.xml", "event.hbm.xml",
                "repertory.hbm.xml", "programme.hbm.xml", "ensemble_event.hbm.xml", "enumeration.hbm.xml",
                "settings.hbm.xml", "imageentity.hbm.xml", "category.hbm.xml", "security.hbm.xml",
                "item.hbm.xml", "form.hbm.xml", "service.hbm.xml", "ftp.hbm.xml", "psyche.hbm.xml"
        };

        if (Boolean.valueOf(System.getProperty("useHbmCache", "true"))) {
            for (String mapping : mappings) {
                addCachedDocument(mapping);
            }
        } else {
            for (String mapping : mappings) {
                InputStream inputStream = getClass().getResourceAsStream(mapping);
                if (inputStream == null) {
                    continue;
                }
                addInputStream(inputStream);
            }
        }
    }

    private void initInterceptors() {
        setInterceptor(new HistoryInterceptor());
    }

    private void initProperties() {
        try {
            setProperty("hibernate.connection.url", SodalisApplication.getDBManager().getConnectionURL());
            setProperty("hibernate.connection.password", new String(CryptoUtils.getDecryptCipher("property.dbPassword").doFinal(CONFIG)));
            setProperty("hibernate.connection.driver_class", SodalisApplication.getDBManager().getDriverClassName());
            setProperty("hibernate.dialect", SodalisApplication.getDBManager().getDialect());
            setProperty("hibernate.show_sql", SodalisApplication.getProperty("hibernate.show_sql", "false"));
        } catch (IllegalBlockSizeException e) {
            LoggerManager.getInstance().error(getClass(), e);
        } catch (BadPaddingException e) {
            LoggerManager.getInstance().error(getClass(), e);
        }
    }

    private void addCachedDocument(String mapping) {
        File cachedFile = new File("data/cache/" + mapping + ".cached");

        if (cachedFile.exists()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(cachedFile));

                addDocument((Document) ois.readObject());
                ois.close();
                return;
            } catch (IOException e) {
                LoggerManager.getInstance().warn(getClass(), e);
            } catch (ClassNotFoundException e) {
                LoggerManager.getInstance().warn(getClass(), e);
            }
        } else {
            cachedFile.getParentFile().mkdirs();
        }

        try {
            InputStream inputStream = getClass().getResourceAsStream(mapping);
            if (inputStream == null) {
                return;
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(cachedFile));
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);

            addDocument(document);

            oos.writeObject(document);
            oos.close();
            return;
        } catch (IOException e) {
            LoggerManager.getInstance().error(getClass(), e);
        } catch (SAXException e) {
            LoggerManager.getInstance().error(getClass(), e);
        } catch (ParserConfigurationException e) {
            LoggerManager.getInstance().error(getClass(), e);
        }

        addInputStream(getClass().getResourceAsStream(mapping));
    }
}