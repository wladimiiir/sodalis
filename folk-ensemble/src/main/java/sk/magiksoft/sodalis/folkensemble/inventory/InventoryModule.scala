package sk.magiksoft.sodalis.folkensemble.inventory

import entity.{InventoryHistoryData, BorrowingInventoryItemData, InventoryItem}
import sk.magiksoft.sodalis.core.module.{DynamicModule, AbstractModule, ModuleDescriptor}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import javax.swing.ImageIcon
import sk.magiksoft.sodalis.category.entity.Category
import collection.JavaConversions._
import sk.magiksoft.sodalis.folkensemble.inventory.data.BorrowerDynamicCategory
import sk.magiksoft.sodalis.core.factory.{EntityFactory, IconFactory}

/**
 * @author wladimiiir
 * @since 2010/5/20
 */

@DynamicModule
class InventoryModule extends AbstractModule {
  private lazy val dynamicCategories = createDynamicCategories
  private lazy val moduleDescriptor = new ModuleDescriptor(IconFactory.getInstance.getIcon("inventoryModule").asInstanceOf[ImageIcon],
    LocaleManager.getString("inventory"))

  EntityFactory.getInstance.registerEntityProperties(classOf[InventoryItem], classOf[BorrowingInventoryItemData], classOf[InventoryHistoryData])
  LocaleManager.registerBundleBaseName("sk.magiksoft.sodalis.folkensemble.locale.inventory")

  def getDataListener = InventoryContextManager

  def getContextManager = InventoryContextManager

  def getModuleDescriptor = moduleDescriptor

  override def getDynamicCategories = {
    dynamicCategories.foreach {
      _.refresh()
    }
    super.getDynamicCategories ++ dynamicCategories
  }

  private def createDynamicCategories = List(new BorrowerDynamicCategory)
}
