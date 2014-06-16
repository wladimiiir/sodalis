
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.enumeration;

import sk.magiksoft.sodalis.core.settings.SettingsPanel;

/**
 * @author wladimiiir
 */
public class DefaultEnumerationInfo implements EnumerationInfo {

    private static final long serialVersionUID = -1l;

    private transient DefaultEnumerationSettingPanel settingPanel;

    @Override
    public Class<? extends EnumerationEntry> getEnumerationEntryClass() {
        return EnumerationEntry.class;
    }

    @Override
    public SettingsPanel getSettingsPanel(Enumeration enumeration) {
        if (settingPanel == null) {
            settingPanel = new DefaultEnumerationSettingPanel(enumeration);
        }

        return settingPanel;
    }

}