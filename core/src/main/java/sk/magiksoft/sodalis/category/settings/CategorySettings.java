package sk.magiksoft.sodalis.category.settings;

import sk.magiksoft.sodalis.core.settings.Settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wladimiiir
 */
public class CategorySettings extends Settings {

    private static CategorySettings instance;

    public static final String O_CATEGORIES_SETTINGSES = "categorySettingses";

    private CategorySettings() {
        super(CategorySettings.class.getName());
    }

    public static synchronized CategorySettings getInstance() {
        if (instance == null) {
            instance = new CategorySettings();
        }
        return instance;
    }

    @Override
    protected Map<String, Object> getDefaultSettingsMap() {
        Map<String, Object> defaultMap = new HashMap<String, Object>();

        defaultMap.put(O_CATEGORIES_SETTINGSES, new ArrayList());

        return defaultMap;
    }


}
