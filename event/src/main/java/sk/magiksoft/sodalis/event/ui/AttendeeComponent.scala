
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.event.ui

import sk.magiksoft.sodalis.event.entity.Attendee
import sk.magiksoft.sodalis.event.EventModule

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 12/2/10
 * Time: 10:49 PM
 * To change this template use File | Settings | File Templates.
 */

class AttendeeComponent(personModuleClass: Class[_ <: Module]) extends ItemComponent[Attendee] {
  private val personChooserEditor = new PersonChooserTableCellEditor(classOf[EventModule], personModuleClass)
  private val attendeeTypeEditor = new ComboBoxTableCellEditor {
    setEditable(true)
  }

  ComboBoxDataManager.getInstance.registerComboBox(Enumerations.ATTENDEE_TYPE, attendeeTypeEditor)

  def createTableModel: ObjectTableModel[Attendee] = new AttendeeTableModel

  def getNewItem = new Attendee

  override def getCellEditor(column: Int) = column match {
    case 0 => personChooserEditor
    case 1 => attendeeTypeEditor
  }

  private class AttendeeTableModel extends ObjectTableModel[Attendee](Array(LocaleManager.getString("person"), LocaleManager.getString("attendeeType"))) {
    def getValueAt(rowIndex: Int, columnIndex: Int) = {
      val attendee = getObject(rowIndex)
      columnIndex match {
        case 0 => attendee.personWrapper
        case 1 => attendee.attendeeType
      }
    }

    override def setValueAt(aValue: AnyRef, rowIndex: Int, columnIndex: Int) = {
      val attendee = getObject(rowIndex)
      columnIndex match {
        case 0 => aValue match {
          case wrapper: PersonWrapper => {
            attendee.personWrapper.setPerson(wrapper.getPerson)
            attendee.personWrapper.setPersonName(wrapper.getPersonName)
            fireTableDataChanged()
          }
          case personName: String => {
            attendee.personWrapper.setPerson(null)
            attendee.personWrapper.setPersonName(personName)
            fireTableDataChanged()
          }
          case _ =>
        }
        case 1 => aValue match {
          case attendeeType: String => {
            attendee.attendeeType = attendeeType
            fireTableDataChanged()
          }
          case _ =>
        }
      }
    }

    override def isCellEditable(rowIndex: Int, columnIndex: Int) = true
  }

}