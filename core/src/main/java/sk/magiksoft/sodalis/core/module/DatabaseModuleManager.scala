package sk.magiksoft.sodalis.core.module

import java.util

import sk.magiksoft.sodalis.core.data.remote.client.ClientDataManager
import sk.magiksoft.sodalis.module.entity.ModuleEntity

import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

/**
 * @author wladimiiir 
 * @since 2014/12/02
 */
class DatabaseModuleManager extends ClientDataManager with ModuleManager {
  private val modules = new ListBuffer[Module]

  loadModules()

  private def loadModules(): Unit = {
    val moduleEntities = getDatabaseEntities(classOf[ModuleEntity])

    modules ++= moduleEntities
      .sortBy(_.order)
      .map(entity => Class.forName(entity.className).newInstance().asInstanceOf[Module])
  }

  override def getModule(index: Int): Module = modules(index)

  override def getModuleBySuperClass[T <: Module](moduleSuperClass: Class[T]): T =
    moduleSuperClass.cast(modules.find(module => moduleSuperClass.isAssignableFrom(module.getClass)).orNull)

  override def getModules: util.List[Module] = modules

  override def isModulePresent(moduleClass: Class[_ <: Module]): Boolean =
    modules.exists(_.getClass == moduleClass)

  override def getModulesCount: Int = modules.size

  override def getModuleByClass[T <: Module](moduleClass: Class[T]): T =
    moduleClass.cast(modules.find(_.getClass == moduleClass).orNull)
}
