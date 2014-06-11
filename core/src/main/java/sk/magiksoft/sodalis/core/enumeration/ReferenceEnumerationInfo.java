
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.core.enumeration;

import sk.magiksoft.sodalis.core.settings.SettingsPanel;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 1/5/11
 * Time: 9:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReferenceEnumerationInfo implements EnumerationInfo{
    @Override
    public Class<? extends EnumerationEntry> getEnumerationEntryClass() {
        return ReferenceEnumerationEntry.class;
    }

    @Override
    public SettingsPanel getSettingsPanel(Enumeration enumeration) {
        return null;
    }
}