
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

import sk.magiksoft.sodalis.core.action.ContextTransferAction;
import sk.magiksoft.sodalis.core.module.Module;
import sk.magiksoft.sodalis.core.security.LoginManagerService;
import sk.magiksoft.sodalis.core.service.RemoteService;
import sk.magiksoft.sodalis.core.service.ServiceManager;
import sk.magiksoft.sodalis.core.splash.SplashLoader;

import javax.swing.*;

/**
 * @author wladimiiir
 */
public interface Controler {
    SplashLoader getSplashLoader();

    String getEnvironmentXML();

    LoginManagerService getLoginManager();

    String getProperty(String key, String defaultValue);

    <T extends RemoteService> T getService(Class<T> serviceClass, String serviceName);

    ServiceManager getServiceManager();

    Module loadModule(Class moduleClass);

    void registerContextTransferAction(final KeyStroke keyStroke, final ContextTransferAction contextAction);

    JPanel getApplicationPanel();

    JMenuBar getMainMenuBar();
}