
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.settings.storage;

import java.util.HashMap;
import java.util.Map;
import sk.magiksoft.sodalis.core.settings.Settings;

/**
 *
 * @author wladimiiir
 */
public class StorageSettings extends Settings{

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