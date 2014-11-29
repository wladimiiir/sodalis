package sk.magiksoft.sodalis.category.entity

import sk.magiksoft.sodalis.core.history.{HistoryEvent, Historizable}
import collection.mutable.ListBuffer
import java.util.{List => jList}
import java.lang.{Long => jLong}
import collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2011/3/17
 */

trait HistorizableMixin extends Historizable {
  var historyEvents = new ListBuffer[HistoryEvent]

  def addHistoryEvent(event: HistoryEvent) {
    historyEvents += event
  }

  def getHistoryEvents(entityID: jLong) = bufferAsJavaList(historyEvents)

  def getHistoryEvents = bufferAsJavaList(historyEvents)

  def setHistoryEvents(jHistoryEvents: jList[HistoryEvent]) {
    historyEvents = new ListBuffer[HistoryEvent] ++ asScalaBuffer(jHistoryEvents)
  }
}
