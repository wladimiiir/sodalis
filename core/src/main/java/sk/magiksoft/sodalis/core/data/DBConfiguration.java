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
}
