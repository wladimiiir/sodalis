package sk.magiksoft.sodalis.core.data;

import org.hibernate.cfg.AvailableSettings;
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
 * @author wladimiiir
 * @since 2010/5/18
 */
public class DBConfiguration extends Configuration {
    private static final byte[] CONFIG = new byte[]{-118, -71, 97, 108, -41, 7, -91, -20, -104, -15, 86, 63, 18, 89, 24, 118};

    public DBConfiguration() {
        initProperties();
        initInterceptors();
        initMappings();
        configure();
    }

    private void initMappings() {
        String[] mappings = new String[]{
                "sodalis.hbm.xml", "person.hbm.xml", "ensemble.hbm.xml", "inventory.hbm.xml", "event.hbm.xml",
                "repertory.hbm.xml", "programme.hbm.xml", "ensemble_event.hbm.xml", "enumeration.hbm.xml",
                "settings.hbm.xml", "imageentity.hbm.xml", "category.hbm.xml", "security.hbm.xml",
                "item.hbm.xml", "form.hbm.xml", "service.hbm.xml", "ftp.hbm.xml", "psyche.hbm.xml"
        };

        if (Boolean.valueOf(System.getProperty("useHbmCache", "false"))) {
            for (String mapping : mappings) {
                addCachedDocument(mapping);
            }
        } else {
            for (String mapping : mappings) {
                InputStream inputStream = getClass().getResourceAsStream(getMappingResource(mapping));
                if (inputStream == null) {
                    continue;
                }
                addInputStream(inputStream);
            }
        }
    }

    private String getMappingResource(String mapping) {
        final String mappingPackagePath = "/sk/magiksoft/sodalis/mapping/";
        return mappingPackagePath + mapping;
    }

    private void initInterceptors() {
        setInterceptor(new HistoryInterceptor());
    }

    private void initProperties() {
        setProperty("hibernate.connection.url", SodalisApplication.getDBManager().getConnectionURL());
        setProperty("hibernate.connection.password", getPassword());
        setProperty("hibernate.connection.driver_class", SodalisApplication.getDBManager().getDriverClassName());
        setProperty("hibernate.dialect", SodalisApplication.getDBManager().getDialect());
        setProperty("hibernate.show_sql", SodalisApplication.getProperty("hibernate.show_sql", "false"));
        setProperty(AvailableSettings.CACHE_REGION_FACTORY, "org.hibernate.cache.ehcache.EhCacheRegionFactory");
//            setProperty("hibernate.use_second_level_cache", SodalisApplication.getProperty("hibernate.use_second_level_cache", "true"));
    }

    public String getUsername() {
        return getProperty("connection.username");
    }

    public String getPassword() {
        try {
            return new String(CryptoUtils.getDecryptCipher("property.dbPassword").doFinal(CONFIG));
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            return "";
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
            } catch (IOException | ClassNotFoundException e) {
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
        } catch (IOException | ParserConfigurationException | SAXException e) {
            LoggerManager.getInstance().error(getClass(), e);
        }

        addInputStream(getClass().getResourceAsStream(mapping));
    }
}
