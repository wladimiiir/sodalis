
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.folkensemble.inventory

import entity.{InventoryHistoryData, BorrowingInventoryItemData, InventoryItem}
import sk.magiksoft.sodalis.core.module.ModuleDescriptor
import sk.magiksoft.sodalis.core.locale.LocaleManager
import javax.swing.ImageIcon
import sk.magiksoft.sodalis.category.entity.Category
import collection.JavaConversions._
import sk.magiksoft.sodalis.folkensemble.inventory.data.BorrowerDynamicCategory
import sk.magiksoft.sodalis.core.factory.IconFactory

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: May 20, 2010
 * Time: 1:03:16 PM
 * To change this template use File | Settings | File Templates.
 */

class InventoryModule extends AbstractModule {
  private lazy val dynamicCategories = createDynamicCategories
  private lazy val moduleDescriptor = new ModuleDescriptor(IconFactory.getInstance.getIcon("inventoryModule").asInstanceOf[ImageIcon],
    LocaleManager.getString("inventory"))

  EntityFactory.getInstance.registerEntityProperties(classOf[InventoryItem], classOf[BorrowingInventoryItemData], classOf[InventoryHistoryData])
  LocaleManager.registerBundleBaseName("sk.magiksoft.sodalis.folkensemble.inventory.locale.inventory")

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