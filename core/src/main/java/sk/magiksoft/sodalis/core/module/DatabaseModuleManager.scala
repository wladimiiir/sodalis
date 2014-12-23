package sk.magiksoft.sodalis.core.module

import java.util

import sk.magiksoft.sodalis.core.ApplicationContainer
import sk.magiksoft.sodalis.core.data.{DefaultDataManager, DBManagerProvider}
import sk.magiksoft.sodalis.core.data.remote.client.ClientDataManager
import sk.magiksoft.sodalis.core.data.remote.server.DataManager
import sk.magiksoft.sodalis.module.entity.ModuleEntity

import scala.collection.JavaConversions
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

/**
 * @author wladimiiir 
 * @since 2014/12/02
 */
class DatabaseModuleManager() extends ModuleManager {
  private val dataManager = DefaultDataManager.getInstance()
  private val modules = new ListBuffer[Module]

  loadModules()

  private def loadModules(): Unit = {
    val moduleEntities = getModuleEntities

    modules ++= moduleEntities
      .filter(entity => {
      try {
        Class.forName(entity.className)
        true
      } catch {
        case e: ClassNotFoundException => false
      }
    })
      .sortBy(_.order)
      .map(entity => Class.forName(entity.className).newInstance().asInstanceOf[Module])
  }

  private def reloadModules(): Unit = {
    modules.clear()
    loadModules()
  }

  def addModule(module: ModuleEntity): Unit = {
    dataManager.addDatabaseEntity(module)
    reloadModules()
  }
  
  def updateModule(module: ModuleEntity): Unit = {
    dataManager.updateDatabaseEntity(module)
    reloadModules()
  }

  def getModuleEntities: List[ModuleEntity] = JavaConversions.asScalaBuffer(dataManager.getDatabaseEntities(classOf[ModuleEntity])).toList

  override def getModuleBySuperClass[T <: Module](moduleSuperClass: Class[T]): T =
    moduleSuperClass.cast(modules.find(module => moduleSuperClass.isAssignableFrom(module.getClass)).orNull)

  override def getAllModules: util.List[Module] = modules

  override def getVisibleModules: util.List[Module] = modules.filter(_.getClass.isAnnotationPresent(classOf[VisibleModule]))

  override def isModulePresent(moduleClass: Class[_ <: Module]): Boolean =
    modules.exists(_.getClass == moduleClass)

  override def getModuleByClass[T <: Module](moduleClass: Class[T]): T =
    moduleClass.cast(modules.find(_.getClass == moduleClass).orNull)
}
