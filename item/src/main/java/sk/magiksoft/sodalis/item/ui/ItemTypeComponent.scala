package sk.magiksoft.sodalis.item.ui

import sk.magiksoft.sodalis.item.entity.ItemType
import sk.magiksoft.swing.itemcomponent.ItemComponent
import sk.magiksoft.sodalis.core.table.ObjectTableModel
import sk.magiksoft.sodalis.core.locale.LocaleManager

/**
 * @author wladimiiir
 * @since 2010/6/10
 */

class ItemTypeComponent(itemTypeKey: String) extends ItemComponent[ItemType] {
  def this() = this(null)

  def createTableModel = new ItemTypeTableModel

  def getNewItem = {
    var itemType = new ItemType
    itemType.key = itemTypeKey
    itemType
  }

  class ItemTypeTableModel extends ObjectTableModel[ItemType](Array(LocaleManager.getString("name"))) {
    def getValueAt(rowIndex: Int, columnIndex: Int) = {
      var itemType = objects.get(rowIndex)

      columnIndex match {
        case 0 => itemType.name
        case _ => ""
      }
    }

    override def isCellEditable(rowIndex: Int, columnIndex: Int) = columnIndex == 0

    override def setValueAt(aValue: Any, rowIndex: Int, columnIndex: Int) = {
      var itemType = objects.get(rowIndex)

      columnIndex match {
        case 0 => itemType.name = aValue.toString
        case _ =>
      }
    }
  }

}
