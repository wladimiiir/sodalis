
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.printing;

/**
 *
 * @author wladimiiir
 */
public interface TableSettingsListener {

    public void tableSettingsDeleted(TablePrintSettings settings);
    void tableSettingsSaved(TablePrintSettings settings);
}