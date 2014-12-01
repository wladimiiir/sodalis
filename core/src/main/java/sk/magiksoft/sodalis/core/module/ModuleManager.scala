package sk.magiksoft.sodalis.core.module

/**
 * @author wladimiiir
 * @since 2014/12/01
 */
trait ModuleManager {
  def getModule(index: Int): Module

  def getModuleByClass[T <: Module](moduleClass: Class[T]): T

  def getModuleBySuperClass[T <: Module](moduleSuperClass: Class[T]): T

  def isModulePresent(moduleClass: Class[_ <: Module]): Boolean

  def getModules: java.util.List[Module]

  def getModulesCount: Int
}
