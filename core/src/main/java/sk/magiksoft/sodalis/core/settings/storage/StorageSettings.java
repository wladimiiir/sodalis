package sk.magiksoft.sodalis.core.settings.storage;

import sk.magiksoft.sodalis.core.settings.Settings;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wladimiiir
 */
public class StorageSettings extends Settings {

    public static final String O_STORAGE_MAP = "storageMap";

    private static StorageSettings instance;

    private StorageSettings() {
        super(StorageSettings.class.getName());
    }

    public static synchronized StorageSettings getInstance() {
        if (instance == null) {
            instance = new StorageSettings();
        }
        return instance;
    }

    @Override
    protected Map<String, Object> getDefaultSettingsMap() {
        Map<String, Object> defaultMap = new HashMap<String, Object>();

        defaultMap.put(O_STORAGE_MAP, new HashMap<String, Storage>());

        return defaultMap;
    }

}
