package sk.magiksoft.sodalis.form.ui

import sk.magiksoft.sodalis.form.entity.Form
import sk.magiksoft.sodalis.core.table.ObjectTableModel
import sk.magiksoft.swing.ISTable
import sk.magiksoft.sodalis.core.locale.LocaleManager

/**
 * @author wladimiiir
 * @since 2010/8/6
 */

class FormTableModel extends ObjectTableModel[Form](Array(
  LocaleManager.getString("name"),
  LocaleManager.getString("description"),
  LocaleManager.getString("pageSize")
), Array(
  ISTable.LEFT_ALIGNMENT_CLASS,
  ISTable.LEFT_ALIGNMENT_CLASS,
  ISTable.CENTER_ALIGNMENT_CLASS
)) {
  def getValueAt(rowIndex: Int, columnIndex: Int) = {
    val form = getObject(rowIndex)

    columnIndex match {
      case 0 => form.name
      case 1 => form.description
      case 2 => form.pages.size.toString
      case _ => ""
    }
  }
}
