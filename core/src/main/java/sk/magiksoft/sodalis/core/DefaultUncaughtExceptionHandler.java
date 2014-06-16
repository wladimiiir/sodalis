
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;

import javax.swing.*;

/**
 * @author wladimiiir
 */
public class DefaultUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final boolean SHOW_DIALOG = Boolean.getBoolean("showErrorDialog");

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LoggerManager.getInstance().error(t.getStackTrace()[0].getClassName(), e);
        if (SHOW_DIALOG) {
            ISOptionPane.showMessageDialog(null, e.getClass().getName() + ": " + e.getLocalizedMessage(), LocaleManager.getString("error"), JOptionPane.ERROR_MESSAGE);
        }
    }

}