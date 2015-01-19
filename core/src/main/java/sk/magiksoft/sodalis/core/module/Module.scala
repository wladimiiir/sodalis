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
   * Installs the module into the application, e.g. prepare database schema, import enumerations...
   * @param classLoader class loader with loaded module jar classes
   */
  def install(dbManager: DBManager)

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
