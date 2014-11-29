package sk.magiksoft.sodalis.core.utils

import java.util.Calendar
import collection.mutable.ListBuffer

/**
 * @author wladimiiir
 * @since 2011/2/10
 */
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
