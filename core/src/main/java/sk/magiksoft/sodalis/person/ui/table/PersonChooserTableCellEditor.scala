
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.person.ui.table

import sk.magiksoft.sodalis.core.module.Module
import sk.magiksoft.sodalis.person.ui.PersonChooserComponent
import javax.swing.table.TableCellEditor
import sk.magiksoft.sodalis.core.utils.Conversions._
import sk.magiksoft.sodalis.person.entity.PersonWrapper
import javax.swing.{BorderFactory, JTable, AbstractCellEditor}
import java.util.EventObject
import java.awt.event.MouseEvent
import javax.swing.event.{ChangeEvent, EventListenerList, CellEditorListener}
import swing.event.FocusLost

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 12/3/10
 * Time: 1:40 PM
 * To change this template use File | Settings | File Templates.
 */

class PersonChooserTableCellEditor(fromModuleClass:Class[_ <: Module], personModuleClass:Class[_ <: Module]) extends PersonChooserComponent(fromModuleClass, personModuleClass) with TableCellEditor {
  private val listenerList = new EventListenerList

  border = BorderFactory.createEmptyBorder
  reactions += {
    case FocusLost(_, oppositeComponent, false) => oppositeComponent match {
      case Some(component) => component==personField.getPopupMenu match {
        case true =>
        case false => stopCellEditing
      } 
      case None =>
    }
  }
  
  override def isCellEditable(e: EventObject) = e match {
    case mouseEvent: MouseEvent => mouseEvent.getClickCount match {
      case 2 => true
      case _ => false
    }
    case _ => true
  }

  def getCellEditorValue = getPersonWrapper

  def removeCellEditorListener(l: CellEditorListener) = listenerList.remove(classOf[CellEditorListener], l)

  def addCellEditorListener(l: CellEditorListener) = listenerList.add(classOf[CellEditorListener], l)

  def cancelCellEditing = fireEditingCancelled

  def stopCellEditing = {
    fireEditingStopped
    true
  }

  def shouldSelectCell(anEvent: EventObject) = true

  def getTableCellEditorComponent(table: JTable, value: AnyRef, isSelected: Boolean, row: Int, column: Int) = {
    setPersonWrapper(value.asInstanceOf[PersonWrapper])
    this
  }

  private def fireEditingStopped = {
    val event = new ChangeEvent(this)
    val listeners = listenerList.getListeners(classOf[CellEditorListener])
    for (listener <- listeners) {
      listener.editingStopped(event)
    }
  }

  private def fireEditingCancelled = {
    val event = new ChangeEvent(this)
    val listeners = listenerList.getListeners(classOf[CellEditorListener])
    for (listener <- listeners) {
      listener.editingCanceled(event)
    }
  }
}