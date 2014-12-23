package sk.magiksoft.sodalis.core

import org.jdesktop.application.Application
import sk.magiksoft.sodalis.category.CategoryModule
import sk.magiksoft.sodalis.core.data.DBManagerProvider
import sk.magiksoft.sodalis.core.injector.Injector
import sk.magiksoft.sodalis.core.license.LicenseManager
import sk.magiksoft.sodalis.core.module.{Module, DynamicModuleManager, ModuleManager, DatabaseModuleManager}
import sk.magiksoft.sodalis.core.service.{LocalServiceManager, ServiceManager}
import sk.magiksoft.sodalis.core.settings.storage.StorageManager
import sk.magiksoft.sodalis.module.entity.ModuleEntity
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

/**
 * @author wladimiiir 
 * @since 2014/12/21
 */
object SodalisManager {
  val coreModule = new CoreModule
  var serviceManager: ServiceManager = _
  var licenseManager: LicenseManager = _
  var storageManager: StorageManager = _
  var moduleManager: ModuleManager = _

  def start(application: Application): Unit = {
    initServiceManager()
    initLicenseManager()
    initStorageManager(application)
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
    coreModule.registerDBResources(DBManagerProvider.getDBManager)
  }

  private def initLicenseManager(): Unit = {
    licenseManager = new LicenseManager
  }

  private def initStorageManager(application: Application): Unit = {
    storageManager = new StorageManager(application)
    Injector.inject(classOf[StorageManager], storageManager)
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
      module.registerDBResources(dbManager)
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
