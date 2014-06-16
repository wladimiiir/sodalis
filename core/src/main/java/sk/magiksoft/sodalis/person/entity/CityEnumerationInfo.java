
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/


package sk.magiksoft.sodalis.person.entity;

import sk.magiksoft.sodalis.core.enumeration.Enumeration;
import sk.magiksoft.sodalis.core.enumeration.EnumerationEntry;
import sk.magiksoft.sodalis.core.enumeration.EnumerationInfo;
import sk.magiksoft.sodalis.core.settings.SettingsPanel;
import sk.magiksoft.sodalis.person.ui.CityEnumerationSettingsPanel;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 11/17/10
 * Time: 3:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class CityEnumerationInfo implements EnumerationInfo {
    private static final long serialVersionUID = -1l;

    private transient SettingsPanel settingPanel;

    @Override
    public Class<? extends EnumerationEntry> getEnumerationEntryClass() {
        return CityEnumerationEntry.class;
    }

    @Override
    public SettingsPanel getSettingsPanel(Enumeration enumeration) {
        if (settingPanel == null) {
            settingPanel = new CityEnumerationSettingsPanel(enumeration);
        }

        return settingPanel;
    }
}