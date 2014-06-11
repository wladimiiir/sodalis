
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.action;

import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.security.LoginManagerService;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author wladimiiir
 */
public class LogoutAction extends AbstractAction {

    public LogoutAction() {
        super(LocaleManager.getString("logOut"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int result = ISOptionPane.showConfirmDialog(null,
                LocaleManager.getString("logOutConfirm"),
                LocaleManager.getString("logOut"), ISOptionPane.YES_NO_OPTION);

        if (result != ISOptionPane.YES_OPTION) {
            return;
        }

        SodalisApplication.get().getService(LoginManagerService.class, LoginManagerService.SERVICE_NAME).logout();
    }

}