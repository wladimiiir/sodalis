
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

import sk.magiksoft.sodalis.core.license.LicenseManager;
import sk.magiksoft.sodalis.core.module.ModuleManager;
import sk.magiksoft.sodalis.core.service.ServiceManager;
import sk.magiksoft.sodalis.core.settings.storage.StorageManager;

import java.io.File;

/**
 * @author wladimiiir
 */
public interface ManagerContainer {

    File getConfigurationXMLFile();

    ModuleManager getModuleManager();

    ServiceManager getServiceManager();

    StorageManager getStorageManager();

    LicenseManager getLicenseManager();

}