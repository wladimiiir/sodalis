
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

import java.io.Serializable;

/**
 *
 * @author wladimiiir
 */
public interface EnumerationInfo extends Serializable {
    Class<? extends EnumerationEntry> getEnumerationEntryClass();
    public SettingsPanel getSettingsPanel(Enumeration enumeration);
}