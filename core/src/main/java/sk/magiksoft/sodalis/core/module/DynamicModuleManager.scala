package sk.magiksoft.sodalis.core.module

import java.util

import org.reflections.Reflections
import sk.magiksoft.sodalis.category.CategoryModule
import sk.magiksoft.sodalis.core.CoreModule
import sk.magiksoft.sodalis.core.data.DBManager

import scala.collection.JavaConversions
import scala.collection.JavaConversions._
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
      .filter(clazz => clazz.isAnnotationPresent(classOf[VisibleModule]) || clazz.isAnnotationPresent(classOf[InvisibleModule]))
      .foreach(clazz => modules += clazz.newInstance())
  }

  override def getModuleBySuperClass[T <: Module](moduleSuperClass: Class[T]): T =
    moduleSuperClass.cast(modules.find(m => moduleSuperClass.isAssignableFrom(m.getClass)).orNull)

  override def getModuleByClass[T <: Module](moduleClass: Class[T]): T =
    moduleClass.cast(modules.find(_.getClass == moduleClass).orNull)

  override def getAllModules: util.List[Module] =
    JavaConversions.bufferAsJavaList(modules.filter(_.getClass != classOf[CoreModule]))

  override def getVisibleModules: util.List[Module] =
    JavaConversions.bufferAsJavaList(modules.filter(_.getClass.isAnnotationPresent(classOf[VisibleModule])))

  override def isModulePresent(moduleClass: Class[_ <: Module]): Boolean =
    getModuleByClass(moduleClass) != null

  override def initModules(dBManager: DBManager): Unit = {
    val modules = getAllModules.filter(_.getClass != classOf[CoreModule])

    modules.foreach(_.initConfiguration(dBManager.getConfiguration))
    dBManager.resetSessionFactory
    modules.foreach(initModule)

    def initModule(module: Module): Unit = {
      module.prepareDB(dBManager)
      module.startUp()
    }
  }
}
