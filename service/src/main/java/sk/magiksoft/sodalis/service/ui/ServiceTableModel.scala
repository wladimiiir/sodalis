package sk.magiksoft.sodalis.service.ui

import sk.magiksoft.sodalis.service.entity.Service
import sk.magiksoft.swing.ISTable
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.table.ObjectTableModel

/**
 * @author wladimiiir
 * @since 2011/3/10
 */

class ServiceTableModel extends ObjectTableModel[Service](Array(
  LocaleManager.getString("serviceName"),
  LocaleManager.getString("serviceCode"),
  LocaleManager.getString("serviceDescription"),
  LocaleManager.getString("servicePrice")
), Array(
  ISTable.LEFT_ALIGNMENT_CLASS,
  ISTable.LEFT_ALIGNMENT_CLASS,
  ISTable.LEFT_ALIGNMENT_CLASS,
  ISTable.RIGHT_ALIGNMENT_CLASS
)) {

  columnIdentificators = Array("serviceName", "serviceCode", "serviceDescription", "servicePrice")

  def getValueAt(rowIndex: Int, columnIndex: Int) = {
    val service = getObject(rowIndex)
    columnIndex match {
      case 0 => service.name
      case 1 => service.code
      case 2 => service.description
      case 3 => service.price.formattedPrice()
    }
  }
}
