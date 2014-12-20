package sk.magiksoft.sodalis.core.locale;

import java.text.MessageFormat;
import java.util.*;

/**
 * @author wladimiiir
 */
public class LocaleManager {


    private static LocaleManager localeManager = null;
    private List<ResourceBundle> resourceBundles = Collections.synchronizedList(new ArrayList<>());

    private LocaleManager() {
        String[] resourcePaths = new String[]{
                "sk.magiksoft.sodalis.core.locale.sodalis",
                "sk.magiksoft.sodalis.core.locale.tooltips",
                "sk.magiksoft.sodalis.core.update.locale.update",
                "sk.magiksoft.sodalis.core.enumeration.enumeration",
                "sk.magiksoft.sodalis.category.locale.category",
                "sk.magiksoft.sodalis.person.locale.person"
        };

        for (String resourcePath : resourcePaths) {
            try {
                resourceBundles.add(ResourceBundle.getBundle(resourcePath));
            } catch (MissingResourceException ignored) {
            }
        }
    }

    private static LocaleManager getLocaleManager() {
        if (localeManager == null) {
            localeManager = new LocaleManager();
        }

        return localeManager;
    }

    public static void registerBundleBaseName(String baseName) {
        getLocaleManager().resourceBundles.add(ResourceBundle.getBundle(baseName));
    }

    public static String getString(String key) {
        if (key == null) {
            return "";
        }
        for (ResourceBundle resourceBundle : getLocaleManager().resourceBundles) {
            try {
                return resourceBundle.getString(key);
            } catch (MissingResourceException ignored) {
            }
        }

        return "ERROR";
    }

    public static String getString(String key, Object... parameters) {
        for (int i = 0; i < getLocaleManager().resourceBundles.size(); i++) {
            ResourceBundle resourceBundle = getLocaleManager().resourceBundles.get(i);
            try {
                return MessageFormat.format(resourceBundle.getString(key), parameters);
            } catch (IllegalArgumentException | MissingResourceException ignored) {
            }
        }

        return "ERROR";
    }
}
