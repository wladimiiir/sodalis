
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.item.ui

import sk.magiksoft.sodalis.item.entity.{Item, ItemType}
import sk.magiksoft.sodalis.core.table.ObjectTableModel
import sk.magiksoft.utils.StringUtils

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jun 22, 2010
 * Time: 7:06:00 PM
 * To change this template use File | Settings | File Templates.
 */

class ItemTableModel(itemType: ItemType) extends ObjectTableModel[Item](itemType.itemProperties.filter(p => p.tableColumn).map {
  ip => ip.name match {
    case name: String => name
    case _ => " "
  }
}.toArray) {

  columnIdentificators = itemType.itemProperties.filter(p => p.tableColumn).map {
    property => StringUtils.removeDiacritics(property.typeName + "." + property.name)
  }.toArray

  def getValueAt(rowIndex: Int, columnIndex: Int) = {
    val item = getObject(rowIndex)
    val itemProperty = itemType.itemProperties.filter(p => p.tableColumn)(columnIndex)

    itemProperty.getValue(item)
  }
}