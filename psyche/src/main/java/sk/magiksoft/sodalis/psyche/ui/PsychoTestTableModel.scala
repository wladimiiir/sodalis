package sk.magiksoft.sodalis.psyche.ui

import sk.magiksoft.sodalis.psyche.entity.PsychoTest
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.table.ObjectTableModel

/**
 * @author wladimiiir
 * @since 2011/5/13
 */

class PsychoTestTableModel extends ObjectTableModel[PsychoTest](Array(
  LocaleManager.getString("testName"),
  LocaleManager.getString("date")
)) {
  def getValueAt(rowIndex: Int, columnIndex: Int) = {
    val test = getObject(rowIndex)
    columnIndex match {
      case 0 => test.name
      case 1 => ObjectTableModel.DATE_TIME_FORMAT.format(test.date.getTime)
    }
  }
}
