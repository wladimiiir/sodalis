package sk.magiksoft.sodalis.module

import java.io.File
import java.net.URLClassLoader

import org.reflections.Reflections
import org.reflections.util.ConfigurationBuilder
import sk.magiksoft.sodalis.core.Constants
import sk.magiksoft.sodalis.core.data.DBManager
import sk.magiksoft.sodalis.core.module.{Module, VisibleModule}
import sk.magiksoft.sodalis.core.utils.FileUtils
import sk.magiksoft.sodalis.module.entity.ModuleEntity

import scala.collection.JavaConversions._

/**
 * @author Y12370
 * @since  2014/12/15
 */
object ModuleLoader {
  def installModules(file: File, dbManager: DBManager): List[ModuleEntity] = {
    val moduleDir = extractModuleArchive(file)
    val classLoader = createModulesClassLoader(moduleDir)
    val modules = loadModules(classLoader)

    modules.foreach(_.getModule(classLoader).install(classLoader, dbManager))
    moduleDir.listFiles().filter(_.getName.endsWith(".jar")).foreach { moduleFile =>
      FileUtils.copyFile(moduleFile, new File(Constants.LIBRARIES_DIRECTORY, moduleFile.getName))
    }
    moduleDir.listFiles().filter(_.getName == "lib").foreach { libDirectory =>
      FileUtils.copyDirectory(libDirectory, new File(Constants.LIBRARIES_DIRECTORY), true)
    }
    modules
  }

  def loadModules(file: File): List[ModuleEntity] = {
    val moduleDir = extractModuleArchive(file)
    val classLoader = createModulesClassLoader(moduleDir)

    loadModules(classLoader)
  }

  private def extractModuleArchive(file: File): File = {
    val moduleDir = new File(Constants.MODULE_TEMP_DIR)

    FileUtils.deleteDir(moduleDir)
    FileUtils.unpackZipFile(new File(file.toURI), moduleDir)
    moduleDir
  }

  private def createModulesClassLoader(moduleDir: File): URLClassLoader = {
    URLClassLoader.newInstance(moduleDir.listFiles().filter(_.getName.endsWith(".jar")).map(_.toURI.toURL), ClassLoader.getSystemClassLoader)
  }

  private def loadModules(classLoader: URLClassLoader): List[ModuleEntity] = {
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
}
