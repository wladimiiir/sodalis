package sk.magiksoft.sodalis.core.module

import sk.magiksoft.sodalis.core.context.ContextManager
import sk.magiksoft.sodalis.core.data.DataListener
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
   * Plugs in the module into the application, e.g. prepare database schema, import enumerations...
   * @param classLoader class loader with loaded module jar classes
   */
  def plugInModule(classLoader: ClassLoader)

  /**
   * Point to initialize the module resources on application start.
   */
  def startUp(): Unit

  /**
   * Point to initialize the module resources, after all modules have been initialized.
   */
  def postStartUp(): Unit

  def getDynamicCategories: List[Category]

  def registerDynamicCategory(dynamicCategory: Category): Unit
}
