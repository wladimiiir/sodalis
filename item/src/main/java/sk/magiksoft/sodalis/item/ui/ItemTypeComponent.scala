
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.item.ui

import sk.magiksoft.swing.itemcomponent.ItemComponent
import sk.magiksoft.sodalis.core.table.ObjectTableModel
import sk.magiksoft.sodalis.item.entity.ItemType
import sk.magiksoft.sodalis.core.locale.LocaleManager

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jun 10, 2010
 * Time: 12:59:13 PM
 * To change this template use File | Settings | File Templates.
 */

class ItemTypeComponent(itemTypeKey: String) extends ItemComponent[ItemType] {
  def this() = this (null)

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