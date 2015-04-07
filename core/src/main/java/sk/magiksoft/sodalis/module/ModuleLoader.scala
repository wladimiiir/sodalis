package sk.magiksoft.sodalis.module

import java.io.File
import java.net.{URL, URLClassLoader}

import org.reflections.Reflections
import org.reflections.util.ConfigurationBuilder
import sk.magiksoft.sodalis.core.{CoreModule, Constants}
import sk.magiksoft.sodalis.core.data.DBManager
import sk.magiksoft.sodalis.core.module.{OverridesModule, InvisibleModule, Module, VisibleModule}
import sk.magiksoft.sodalis.core.utils.FileUtils
import sk.magiksoft.sodalis.module.entity.ModuleEntity

import scala.collection.JavaConversions._

/**
 * @author Y12370
 * @since  2014/12/15
 */
object ModuleLoader {
  def installModules(file: File, dbManager: DBManager): List[ModuleEntity] = {
    def isOverriden(entity: ModuleEntity, entities: List[ModuleEntity]): Boolean = {
      entities
        .map(_.getModule.getClass)
        .filter(_.isAnnotationPresent(classOf[OverridesModule]))
        .map(_.getAnnotation(classOf[OverridesModule]))
        .exists(_.value() == entity.getModule.getClass)
    }

    val moduleDir = extractModuleArchive(file)
    val classLoader = createModulesClassLoader(moduleDir, loadLibrary = true)
    val moduleEntities = loadModuleEntities(classLoader)
    val moduleEntitiesToInstall = moduleEntities
      .filter(!isOverriden(_, moduleEntities))

    moduleEntitiesToInstall
      .foreach(_.getModule.prepareDB(dbManager))

    moduleDir.listFiles().filter(_.getName.endsWith(".jar")).foreach { moduleFile =>
      FileUtils.copyFile(moduleFile, new File(Constants.LIBRARIES_DIRECTORY, moduleFile.getName))
    }
    moduleDir.listFiles().filter(_.getName == "lib").foreach { libDirectory =>
      FileUtils.copyDirectory(libDirectory, new File(Constants.LIBRARIES_DIRECTORY), true)
    }

    moduleEntitiesToInstall
  }

  private def loadModuleEntities(classLoader: URLClassLoader): List[ModuleEntity] = {
    val reflections = new Reflections(ConfigurationBuilder.build(classLoader))

    val entities = reflections.getSubTypesOf(classOf[Module])
      .filter(clazz => clazz.isAnnotationPresent(classOf[VisibleModule]) || clazz.isAnnotationPresent(classOf[InvisibleModule]))
      .filter(clazz => clazz != classOf[CoreModule])
      .zipWithIndex
      .map { case (moduleClass: Class[_], index: Int) =>
      val entity = new ModuleEntity
      entity.order = index
      entity.className = moduleClass.getName
      entity
    }.toList

    entities.foreach(_.getModule(classLoader))
    entities
  }

  private def extractModuleArchive(file: File): File = {
    val moduleDir = new File(Constants.MODULE_TEMP_DIR)

    FileUtils.deleteDir(moduleDir)
    FileUtils.unpackZipFile(new File(file.toURI), moduleDir)
    moduleDir
  }

  private def createModulesClassLoader(moduleDir: File, loadLibrary: Boolean): URLClassLoader = {
    val moduleJars = moduleDir.listFiles().filter(_.getName.endsWith(".jar")).map(_.toURI.toURL)
    val libJars = loadLibrary match {
      case true =>
        val libDir = new File(moduleDir, Constants.LIBRARIES_DIRECTORY)
        if (libDir != null)
          libDir.listFiles().filter(_.getName.endsWith(".jar")).map(_.toURI.toURL).toList
        else
          Nil

      case false => Nil
    }

    URLClassLoader.newInstance(moduleJars ++ libJars, ClassLoader.getSystemClassLoader)
  }
}
