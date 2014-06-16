/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.category.entity

import sk.magiksoft.sodalis.core.history.{HistoryEvent, Historizable}
import collection.mutable.ListBuffer
import java.util.{List => jList}
import java.lang.{Long => jLong}
import collection.JavaConversions._

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 3/17/11
 * Time: 5:20 PM
 * To change this template use File | Settings | File Templates.
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