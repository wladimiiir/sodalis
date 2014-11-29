package sk.magiksoft.sodalis.folkensemble.inventory.ui

import sk.magiksoft.sodalis.folkensemble.inventory.entity.{BorrowingInventoryItemData, InventoryItem}
import sk.magiksoft.sodalis.folkensemble.inventory.entity.BorrowingInventoryItemData.InventoryItemState
import sk.magiksoft.sodalis.core.table.ObjectTableModel
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.item.entity.Item

/**
 * @author wladimiiir
 * @since 2010/7/31
 */

class GeneralInventoryItemTableModel extends ObjectTableModel[Item](
  Array(
    LocaleManager.getString("inventoryItem"),
    LocaleManager.getString("evidenceNumber"),
    LocaleManager.getString("description"),
    LocaleManager.getString("inventoryItem.state")
  )) {

  columnIdentificators = Array("inventoryItem", "evidenceNumber", "description", "state")

  def getValueAt(rowIndex: Int, columnIndex: Int) = {
    val item = getObject(rowIndex).asInstanceOf[InventoryItem]

    columnIndex match {
      case 0 => item.itemType.name
      case 1 => item.getPropertyTypeValues("EvidenceNumber").headOption match {
        case Some(value) => value.value.toString
        case None => ""
      }
      case 2 => item.getHTMLInfoString
      case 3 => {
        val inventoryItemData = item.getInventoryItemData(classOf[BorrowingInventoryItemData])
        val state = inventoryItemData.getState

        state match {
          case InventoryItemState.BORROWED => state + " (" + inventoryItemData.getCurrentBorrowing.getBorrowerName + ")"
          case _ => state
        }
      }
    }
  }
}
