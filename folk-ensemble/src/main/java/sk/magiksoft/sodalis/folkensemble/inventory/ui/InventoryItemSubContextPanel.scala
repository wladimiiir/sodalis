package sk.magiksoft.sodalis.folkensemble.inventory.ui

import sk.magiksoft.sodalis.item.ui.{ItemTypeTablePanel, ItemSubContextPanel}
import sk.magiksoft.sodalis.folkensemble.inventory.InventoryModule
import javax.swing.event.{TreeSelectionEvent, TreeSelectionListener}
import sk.magiksoft.sodalis.item.entity.ItemType

/**
 * @author wladimiiir
 * @since 2010/7/31
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
