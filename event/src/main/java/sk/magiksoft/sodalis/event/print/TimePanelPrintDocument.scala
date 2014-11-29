package sk.magiksoft.sodalis.event.print

import sk.magiksoft.sodalis.event.entity.Event
import collection.mutable.HashMap
import java.util.Calendar
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder
import net.sf.jasperreports.engine.xml.JasperDesignFactory
import net.sf.jasperreports.engine.design.JasperDesign
import net.sf.jasperreports.engine.JRPrintPage
import sk.magiksoft.sodalis.core.utils.CalendarRange

/**
 * @author wladimiiir
 * @since 2011/2/10
 */
class TimePanelPrintDocument(events: List[Event], range: CalendarRange) {

  private def createRectangles = {
    val map = new HashMap[Calendar, List[Event]]
    val day = range.from.clone.asInstanceOf[Calendar]
    day.set(Calendar.HOUR_OF_DAY, range.to.get(Calendar.HOUR_OF_DAY))
    day.set(Calendar.MINUTE, range.to.get(Calendar.MINUTE))
    day.set(Calendar.SECOND, range.to.get(Calendar.SECOND))
    day.set(Calendar.MILLISECOND, range.to.get(Calendar.MILLISECOND))

    while (!day.after(range.to)) {
      map.put(day.clone.asInstanceOf[Calendar], events.filter(_.acceptDay(day)))
      day.add(Calendar.DATE, 1)
    }

  }

  def print = {

  }
}
