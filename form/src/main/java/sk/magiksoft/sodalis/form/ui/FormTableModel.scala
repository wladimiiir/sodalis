
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.form.ui

import sk.magiksoft.sodalis.form.entity.Form
import sk.magiksoft.sodalis.core.table.ObjectTableModel

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Aug 6, 2010
 * Time: 9:30:06 AM
 * To change this template use File | Settings | File Templates.
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