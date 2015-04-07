package sk.magiksoft.sodalis.core.module

import org.hibernate.cfg.Configuration
import sk.magiksoft.sodalis.core.context.ContextManager
import sk.magiksoft.sodalis.core.data.{DBManager, DataListener}
import sk.magiksoft.sodalis.category.entity.Category

/**
 * @author wladimiiir
 * @since 2011/4/22
 */

trait Module {

  def getModuleDescriptor: ModuleDescriptor

  def getContextManager: ContextManager

  def getDataListener: DataListener

  /**
   * Prepares DB schema for module.
   */
  def prepareDB(dbManager: DBManager)

  /**
   * Does the installation of the module, e.g. importing enumerations.
   */
  def install(dBManager: DBManager)

  /**
   * Point to initialize the module resources on application start.
   */
  def startUp(): Unit

  /**
   * Registers resources for database manager.
   * @param configuration DB manager to register module resources for
   */
  def initConfiguration(configuration: Configuration): Unit

  def getDynamicCategories: List[Category]

  def registerDynamicCategory(dynamicCategory: Category): Unit
}
