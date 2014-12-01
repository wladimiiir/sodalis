package sk.magiksoft.sodalis.core.module

import java.util

import org.reflections.Reflections

import scala.collection.JavaConversions
import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.ReflectionUtils

/**
 * @author wladimiiir
 * @since 2014/12/01
 */
class DynamicModuleManager extends ModuleManager {
  private val modules = new ListBuffer[Module]

  loadModules()

  private def loadModules(): Unit = {
    val reflections = new Reflections()
    val moduleClasses = JavaConversions.asScalaSet(reflections.getSubTypesOf(classOf[Module]))

    moduleClasses
      .foreach(clazz => modules += clazz.newInstance())
  }

  override def getModule(index: Int): Module =
    modules(index)

  override def getModuleBySuperClass[T <: Module](moduleSuperClass: Class[T]): T =
    moduleSuperClass.cast(modules.find(m => moduleSuperClass.isAssignableFrom(m.getClass)).orNull)

  override def getModuleByClass[T <: Module](moduleClass: Class[T]): T =
    moduleClass.cast(modules.find(_.getClass == moduleClass).orNull)

  override def getModules: util.List[Module] =
    JavaConversions.bufferAsJavaList(modules)

  override def isModulePresent(moduleClass: Class[_ <: Module]): Boolean =
    getModuleByClass(moduleClass) != null

  override def getModulesCount: Int =
    modules.size

}
