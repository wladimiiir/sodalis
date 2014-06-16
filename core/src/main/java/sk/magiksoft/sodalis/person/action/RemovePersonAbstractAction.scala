
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.person.action

import java.awt.event.ActionEvent
import sk.magiksoft.sodalis.person.entity.Person
import javax.swing.JOptionPane
import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.ui.ISOptionPane
import sk.magiksoft.sodalis.core.data.DefaultDataManager
import sk.magiksoft.sodalis.core.factory.IconFactory
import sk.magiksoft.sodalis.core.action.{ActionMessage, EntityAction, MessageAction}
import collection.JavaConversions._
import java.util.{List => jList}

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 12/16/10
 * Time: 12:19 PM
 * To change this template use File | Settings | File Templates.
 */

abstract class RemovePersonAbstractAction
  extends MessageAction(IconFactory.getInstance.getIcon("remove")) with EntityAction[Person] {
  protected var persons = List.empty[Person]

  def getActionMessage(objects: jList[_]) = {
    persons = objects.filter {
      _.isInstanceOf[Person]
    }.map {
      _.asInstanceOf[Person]
    }.filter(filterPersons).toList

    persons match {
      case Nil => new ActionMessage(false, getNoSelectionMessage)
      case person :: Nil => new ActionMessage(true, getSingleSelectionMessage)
      case _ => new ActionMessage(true, getMultipleSelectionMessage)
    }
  }

  protected def filterPersons: Person => Boolean

  protected def getNoSelectionMessage: String

  protected def getSingleSelectionMessage: String

  protected def getMultipleSelectionMessage: String

  protected def getDeleteConfirmSingleSelectionMessage: String

  protected def getDeleteConfirmMultipleSelectionMessage: String

  def actionPerformed(e: ActionEvent) {
    apply(persons)
  }

  def apply(entities: List[Person]) {
    val result = entities match {
      case Nil => JOptionPane.NO_OPTION
      case person :: Nil => ISOptionPane.showConfirmDialog(SodalisApplication.get.getMainFrame,
        getDeleteConfirmSingleSelectionMessage, entities(0).getFullName(true), JOptionPane.YES_NO_OPTION)
      case _ => ISOptionPane.showConfirmDialog(SodalisApplication.get.getMainFrame,
        getDeleteConfirmMultipleSelectionMessage, LocaleManager.getString("delete"), JOptionPane.YES_NO_OPTION)
    }

    result match {
      case JOptionPane.NO_OPTION =>
      case JOptionPane.YES_OPTION => {
        for (person <- entities) {
          person.setDeleted(true)
          setupDeletedPerson(person)
          DefaultDataManager.getInstance.updateDatabaseEntity(person)
        }
      }
    }
  }

  def getName(entities: List[Person]) = entities match {
    case Nil => getSingleSelectionMessage
    case person :: Nil => getSingleSelectionMessage
    case _ => getMultipleSelectionMessage
  }

  def isAllowed(entities: List[Person]) = entities match {
    case Nil => false
    case _ => true
  }

  protected def setupDeletedPerson(person: Person)
}