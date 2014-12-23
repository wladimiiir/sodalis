package sk.magiksoft.sodalis.core

import sk.magiksoft.sodalis.category.entity.Category
import sk.magiksoft.sodalis.core.context.ContextManager
import sk.magiksoft.sodalis.core.data.{DataListener, DBManager}
import sk.magiksoft.sodalis.core.module.{ModuleDescriptor, Module, InvisibleModule}

/**
 * @author wladimiiir 
 * @since 2014/12/22
 */
@InvisibleModule
class CoreModule extends Module {
  override def getModuleDescriptor: ModuleDescriptor = null

  override def getDataListener: DataListener = null

  override def getContextManager: ContextManager = null

  override def registerDBResources(manager: DBManager): Unit = {
    manager.getConfiguration.addURL(getClass.getResource("data/mapping/sodalis.hbm.xml"))
    manager.getConfiguration.addURL(getClass.getResource("data/mapping/category.hbm.xml"))
    manager.getConfiguration.addURL(getClass.getResource("data/mapping/enumeration.hbm.xml"))
    manager.getConfiguration.addURL(getClass.getResource("data/mapping/imageentity.hbm.xml"))
    manager.getConfiguration.addURL(getClass.getResource("data/mapping/person.hbm.xml"))
    manager.getConfiguration.addURL(getClass.getResource("data/mapping/security.hbm.xml"))
    manager.getConfiguration.addURL(getClass.getResource("data/mapping/settings.hbm.xml"))
  }

  override def getDynamicCategories: List[Category] = List()

  override def registerDynamicCategory(dynamicCategory: Category): Unit = {}

  override def startUp(): Unit = {}

  override def install(classLoader: ClassLoader): Unit = {}
}
