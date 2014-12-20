package sk.magiksoft.sodalis.core;

import sk.magiksoft.sodalis.core.license.LicenseManager;
import sk.magiksoft.sodalis.core.module.ModuleManager;
import sk.magiksoft.sodalis.core.service.ServiceManager;
import sk.magiksoft.sodalis.core.settings.storage.StorageManager;

import java.io.File;
import java.net.URL;

/**
 * @author wladimiiir
 */
public interface ManagerContainer {

    URL getConfigurationURL();

    ModuleManager getModuleManager();

    ServiceManager getServiceManager();

    StorageManager getStorageManager();

    LicenseManager getLicenseManager();

}
