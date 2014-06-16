
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.settings;

import sk.magiksoft.sodalis.core.exception.VetoException;

import javax.security.auth.Subject;
import javax.swing.*;

/**
 * @author wladimiiir
 */
public interface SettingsPanel {

    String getSettingsPanelName();

    JComponent getSwingComponent();

    void setup(Subject subject) throws VetoException;

    void reloadSettings();

    boolean saveSettings();

}