/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.ui

import sk.magiksoft.sodalis.core.table.ObjectTableModel
import sk.magiksoft.sodalis.psyche.entity.PsychoTest
import sk.magiksoft.sodalis.core.locale.LocaleManager

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/13/11
 * Time: 11:21 AM
 * To change this template use File | Settings | File Templates.
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