package sk.magiksoft.sodalis.core

import java.io.File

import org.jdesktop.application.Application
import sk.magiksoft.sodalis.category.CategoryModule
import sk.magiksoft.sodalis.core.data.DBManagerProvider
import sk.magiksoft.sodalis.core.injector.Injector
import sk.magiksoft.sodalis.core.license.LicenseManager
import sk.magiksoft.sodalis.core.module.{DatabaseModuleManager, DynamicModuleManager, Module, ModuleManager}
import sk.magiksoft.sodalis.core.service.{LocalServiceManager, ServiceManager}
import sk.magiksoft.sodalis.core.settings.storage.StorageManager
import sk.magiksoft.sodalis.module.ModuleLoader
import sk.magiksoft.sodalis.module.entity.ModuleEntity

import scala.collection.JavaConversions._

/**
 * @author wladimiiir 
 * @since 2014/12/21
 */
object SodalisManager {
  private val STARTUP_MODULES = "./startup_modules"

  val coreModule = new CoreModule
  var serviceManager: ServiceManager = _
  var licenseManager: LicenseManager = _
  var storageManager: StorageManager = _
  var moduleManager: ModuleManager = _

  def start(application: Application): Unit = {
    initServiceManager()
    initLicenseManager()
    initStorageManager(application)
    installModules()
    initModules()
  }

  def isDebugMode = System.getProperty("debugMode", "FALSE").toBoolean

  private def initServiceManager(): Unit = {
    initCoreModule()
    serviceManager = new LocalServiceManager
    serviceManager.initialize()
  }

  private def initCoreModule() {
    coreModule.startUp()
    coreModule.initConfiguration(DBManagerProvider.getDBManager.getConfiguration)
  }

  private def initLicenseManager(): Unit = {
    licenseManager = new LicenseManager
  }

  private def initStorageManager(application: Application): Unit = {
    storageManager = new StorageManager(application)
    Injector.inject(classOf[StorageManager], storageManager)
  }

  private def installModules(): Unit = {
    def isModule: (File) => Boolean = _.getName.toLowerCase.endsWith("." + Constants.MODULE_FILE_EXTENSION)

    val moduleFiles = new File(STARTUP_MODULES).listFiles()
    if (moduleFiles != null) {
      moduleFiles.filter(isModule).foreach(ModuleLoader.installModules(_, DBManagerProvider.getDBManager))
    }
  }


  private def initModules(): Unit = {
    moduleManager = System.getProperty("dynamicModuleManager", "FALSE").toBoolean match {
      case true => new DynamicModuleManager
      case false =>
        val manager = new DatabaseModuleManager
        if (manager.getAllModules.isEmpty) {
          createStartUpModuleEntities().foreach(manager.addModule)
        }
        manager
    }

    val dbManager = DBManagerProvider.getDBManager

    moduleManager.getAllModules.foreach(initModule)
    def initModule(module: Module): Unit = {
      module.initConfiguration(dbManager.getConfiguration)
      module.startUp()
    }

    dbManager.resetSessionFactory()
  }

  private def createStartUpModuleEntities(): List[ModuleEntity] = {
    List({
      val categoryModuleEntity = new ModuleEntity
      categoryModuleEntity.className = classOf[CategoryModule].getName
      categoryModuleEntity.order = 0
      categoryModuleEntity
    })
  }
}
