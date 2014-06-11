
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 2/15/11
 * Time: 8:32 PM
 */
package sk.magiksoft.sodalis.event.ui

import sk.magiksoft.sodalis.core.ui.AbstractTableContext
import sk.magiksoft.swing.ISTable
import sk.magiksoft.sodalis.core.table.ObjectTableModel
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.event.entity.{EventEntityData, Event}
import java.awt.BorderLayout
import javax.swing.JScrollPane
import sk.magiksoft.sodalis.core.factory.ColorList
import java.util.Comparator
import sk.magiksoft.sodalis.event.{EventContextManager, EventModule}
import sk.magiksoft.sodalis.core.filter.ui.FilterPanel
import sk.magiksoft.sodalis.core.filter.action.FilterEvent
import reflect.BeanProperty


class EventTableContext extends AbstractTableContext(classOf[Event], new ISTable(eventTableModel)){
  private var filterAction = FilterEvent.ACTION_RESET
  private var filterQuery = ""

  initComponents

  private def initComponents = {
    val scrollPane = new JScrollPane(super.getTable) {
      getViewport.setBackground(ColorList.SCROLLPANE_BACKGROUND)
    }
    setLayout(new BorderLayout)
    add(scrollPane, BorderLayout.CENTER)
    initCategoryTreeComponent(classOf[EventModule], scrollPane)
  }
}

private object eventTableModel extends ObjectTableModel[Event](Array(
  LocaleManager.getString("eventName"),
  LocaleManager.getString("place"),
  LocaleManager.getString("eventType"),
  LocaleManager.getString("from"),
  LocaleManager.getString("to"),
  LocaleManager.getString("time")
), Array(
  ISTable.LEFT_ALIGNMENT_CLASS,
  ISTable.LEFT_ALIGNMENT_CLASS,
  ISTable.LEFT_ALIGNMENT_CLASS,
  ISTable.RIGHT_ALIGNMENT_CLASS,
  ISTable.RIGHT_ALIGNMENT_CLASS,
  ISTable.RIGHT_ALIGNMENT_CLASS
)) {

  val fromComparator = new Comparator[Event]{
    def compare(o1: Event, o2: Event) = o1.isRepeating match {
      case true => o2.isRepeating match {
        case true => o1.getRepeatStart.compareTo(o2.getRepeatStart)
        case false => o1.getRepeatStart.compareTo(o2.getStartTime)
      }
      case false => o2.isRepeating match {
        case true => o1.getStartTime.compareTo(o2.getRepeatStart)
        case false => o1.getStartTime.compareTo(o2.getStartTime)
      }
    }
  }
  val toComparator = new Comparator[Event] {
    def compare(o1: Event, o2: Event) = o1.isRepeating match {
      case true => o2.isRepeating match {
        case true => o1.getRepeatEnd.compareTo(o2.getRepeatEnd)
        case false => o1.getRepeatEnd.compareTo(o2.getEndTime)
      }
      case false => o2.isRepeating match {
        case true => o1.getEndTime.compareTo(o2.getRepeatEnd)
        case false => o1.getEndTime.compareTo(o2.getEndTime)
      }
    }
  }
  val timeComparator = new Comparator[Event] {
    def compare(o1: Event, o2: Event) = o1.getStartTime.compareTo(o2.getStartTime)
  }

  override def getComparator(column: Int):Comparator[_] = column match {
    case 3 => fromComparator
    case 4 => toComparator
    case 5 => timeComparator
    case _ => super.getComparator(column)
  }

  def getValueAt(rowIndex: Int, columnIndex: Int) = {
    val event = getObject(rowIndex)
    columnIndex match {
      case 0 => event.getEventName
      case 1 => event.getPlace
      case 2 => event.getEventTypeName
      case 3 => event.isRepeating match {
        case true => ObjectTableModel.DATE_FORMAT.format(event.getRepeatStart.getTime)
        case false => ObjectTableModel.DATE_TIME_FORMAT.format(event.getStartTime.getTime)
      }
      case 4 => event.isRepeating match {
        case true => ObjectTableModel.DATE_FORMAT.format(event.getRepeatEnd.getTime)
        case false => ObjectTableModel.DATE_TIME_FORMAT.format(event.getEndTime.getTime)
      }
      case 5 => ObjectTableModel.TIME_FORMAT.format(event.getStartTime.getTime) +"-"+ObjectTableModel.TIME_FORMAT.format(event.getEndTime.getTime)
      case _ => ""
    }
  }
}