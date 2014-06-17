
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.folkensemble.inventory.ui

import sk.magiksoft.sodalis.item.ui.{ItemTypeTablePanel, ItemSubContextPanel}
import sk.magiksoft.sodalis.folkensemble.inventory.InventoryModule
import javax.swing.event.{TreeSelectionEvent, TreeSelectionListener}
import sk.magiksoft.sodalis.item.entity.ItemType

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jul 31, 2010
 * Time: 10:56:22 AM
 * To change this template use File | Settings | File Templates.
 */

class InventoryItemSubContextPanel(itemTypesKey: String) extends ItemSubContextPanel(itemTypesKey) {
  override protected def getGeneralItemTableModel = Option(new GeneralInventoryItemTableModel)

  override protected def createItemTypeTablePanel(itemType: ItemType) = new ItemTypeTablePanel(itemType, new InventoryItemTableModel(itemType)) {
    initCategoryTreeComponent(classOf[InventoryModule])
    categoryTreeComponent.get.addTreeSelectionListener(new TreeSelectionListener {
      def valueChanged(e: TreeSelectionEvent) = {
        currentObjectChanged
      }
    })
  }

  override protected def createGeneralItemTypeTablePanel(itemType: ItemType) = new ItemTypeTablePanel(itemType, getGeneralItemTableModel.get) {
    initCategoryTreeComponent(classOf[InventoryModule])
    categoryTreeComponent.get.addTreeSelectionListener(new TreeSelectionListener {
      def valueChanged(e: TreeSelectionEvent) = {
        currentObjectChanged
      }
    })
  }
}