package sk.magiksoft.sodalis.module

import java.io.File
import java.net.URLClassLoader

import org.reflections.Reflections
import org.reflections.util.ConfigurationBuilder
import sk.magiksoft.sodalis.core.module.{VisibleModule, Module}
import sk.magiksoft.sodalis.core.utils.FileUtils
import sk.magiksoft.sodalis.module.entity.ModuleEntity
import scala.collection.JavaConversions._

/**
 * @author Y12370
 * @since  2014/12/15
 */
object ModuleLoader {
  private val MODULE_TEMP_DIR: String = "data" + File.separator + "temp" + File.separator + "module"

  private def extractModuleArchive(file: File): File = {
    val moduleDir = new File(MODULE_TEMP_DIR)

    FileUtils.deleteDir(moduleDir)
    FileUtils.unpackZipFile(new File(file.toURI), moduleDir)
    moduleDir
  }

  def loadModules(file: File): List[ModuleEntity] = {
    val moduleDir: File = extractModuleArchive(file)

    val classLoader = URLClassLoader.newInstance(moduleDir.listFiles().filter(_.getName.endsWith(".jar")).map(_.toURI.toURL))
    val reflections = new Reflections(ConfigurationBuilder.build(classLoader))

    reflections.getSubTypesOf(classOf[Module])
      .filter(_.isAnnotationPresent(classOf[VisibleModule]))
      .zipWithIndex
      .map { case (moduleClass: Class[_], index: Int) =>
      val entity = new ModuleEntity
      entity.order = index
      entity.className = moduleClass.getName
      entity
    }.toList

  }

  def plugInModules(file: File, moduleEntities: List[ModuleEntity]): Unit = {
    val moduleDir: File = extractModuleArchive(file)
    val classLoader = URLClassLoader.newInstance(moduleDir.listFiles().filter(_.getName.endsWith(".jar")).map(_.toURI.toURL))

    moduleEntities.foreach(_.getModule.install(classLoader))
  }
}
