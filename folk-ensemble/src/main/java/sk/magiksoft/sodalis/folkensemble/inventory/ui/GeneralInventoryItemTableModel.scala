
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.folkensemble.inventory.ui

import sk.magiksoft.sodalis.core.table.ObjectTableModel
import sk.magiksoft.sodalis.item.entity.Item
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.folkensemble.inventory.entity.{BorrowingInventoryItemData, InventoryItem}
import sk.magiksoft.sodalis.folkensemble.inventory.entity.BorrowingInventoryItemData.InventoryItemState

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jul 31, 2010
 * Time: 9:02:35 AM
 * To change this template use File | Settings | File Templates.
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