
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


/*
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 2/25/11
 * Time: 4:30 PM
 */
package sk.magiksoft.sodalis.core.filter.element

;

class NoComparatorDateColumnComponent extends DateColumnComponent {
  comparatorComboBox.setVisible(false)

  override def getComparator = null

  override def isIncluded = dateChooser.getCalendar != null
}