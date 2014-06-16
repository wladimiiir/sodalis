
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


/*
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 2/10/11
 * Time: 4:46 PM
 */
package sk.magiksoft.sodalis.core.utils

import java.util.Calendar
import collection.mutable.ListBuffer

class CalendarRange(val from: Calendar, val to: Calendar) {
  def getDays: Array[Calendar] = {
    val days = new ListBuffer[Calendar]
    var calendar = from.clone.asInstanceOf[Calendar];

    while (!to.before(calendar)) {
      days += calendar
      calendar = calendar.clone.asInstanceOf[Calendar]
      calendar.add(Calendar.DATE, 1)
    }

    days.toArray
  }
}