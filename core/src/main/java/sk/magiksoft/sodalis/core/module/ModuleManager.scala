package sk.magiksoft.sodalis.core.module

import sk.magiksoft.sodalis.core.data.DBManager

/**
 * @author wladimiiir
 * @since 2014/12/01
 */
trait ModuleManager {
  def getModuleByClass[T <: Module](moduleClass: Class[T]): T

  def getModuleBySuperClass[T <: Module](moduleSuperClass: Class[T]): T

  def isModulePresent(moduleClass: Class[_ <: Module]): Boolean

  def getAllModules: java.util.List[Module]

  def getVisibleModules: java.util.List[Module]

  def initModules(dBManager: DBManager): Unit
}
