package sk.magiksoft.sodalis.module

import java.io.File
import java.net.URLClassLoader

import org.reflections.Reflections
import org.reflections.util.ConfigurationBuilder
import sk.magiksoft.sodalis.core.module.{DynamicModule, Module}
import sk.magiksoft.sodalis.core.utils.FileUtils
import sk.magiksoft.sodalis.module.entity.ModuleEntity
import scala.collection.JavaConversions._

/**
 * @author Y12370
 * @since  2014/12/15
 */
object ModuleLoader {
  private val MODULE_TEMP_DIR: String = "data" + File.separator + "temp" + File.separator + "module"

  def loadModules(file: File): List[ModuleEntity] = {
    val moduleDir = new File(MODULE_TEMP_DIR)

    FileUtils.deleteDir(moduleDir)
    FileUtils.unpackZipFile(new File(file.toURI), moduleDir)

    val classLoader = URLClassLoader.newInstance(moduleDir.listFiles().filter(_.getName.endsWith(".jar")).map(_.toURI.toURL))
    val reflections = new Reflections(ConfigurationBuilder.build(classLoader))


    reflections.getSubTypesOf(classOf[Module])
      .filter(_.isAnnotationPresent(classOf[DynamicModule]))
      .zipWithIndex
      .map { case (moduleClass: Class[_], index: Int) =>
      val entity = new ModuleEntity
      entity.order = index
      entity.className = moduleClass.getName
      entity
    }.toList
  }

  def saveModules(file: File): Unit = {

  }
}
