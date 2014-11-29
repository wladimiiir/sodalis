package sk.magiksoft.sodalis.folkensemble.inventory.ui

import sk.magiksoft.sodalis.folkensemble.inventory.entity.BorrowingInventoryItemData.InventoryItemState
import sk.magiksoft.sodalis.folkensemble.inventory.entity.{BorrowingInventoryItemData, InventoryItem}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.item.ui.ItemTableModel
import sk.magiksoft.sodalis.item.entity.ItemType

/**
 * @author wladimiiir
 * @since 2010/7/31
 */

class InventoryItemTableModel(itemType: ItemType) extends ItemTableModel(itemType) {
  val stateColumnName = LocaleManager.getString("inventoryItem.state")

  override def getColumnCount = super.getColumnCount + 1

  override def getColumnName(column: Int) = if (column == getColumnCount - 1) stateColumnName else super.getColumnName(column)

  override def getValueAt(rowIndex: Int, columnIndex: Int) = if (columnIndex == getColumnCount - 1) {
    val item = getObject(rowIndex).asInstanceOf[InventoryItem]
    val inventoryItemData = item.getInventoryItemData(classOf[BorrowingInventoryItemData])
    val state = inventoryItemData.getState

    state match {
      case InventoryItemState.BORROWED => state + " (" + inventoryItemData.getCurrentBorrowing.getBorrowerName + ")"
      case _ => state.toString
    }
  } else super.getValueAt(rowIndex, columnIndex)
}
