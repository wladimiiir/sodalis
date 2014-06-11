
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 2/16/11
 * Time: 5:37 PM
 */
package sk.magiksoft.sodalis.event.action

import java.awt.event.ActionEvent
import java.util.List
import sk.magiksoft.sodalis.core.action.{ActionMessage, MessageAction}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.event.entity.Event
import collection.mutable.ListBuffer
import collection.JavaConversions._
import sk.magiksoft.sodalis.core.ui.ISOptionPane
import sk.magiksoft.sodalis.core.SodalisApplication
import javax.swing.JOptionPane
import sk.magiksoft.sodalis.event.data.EventDataManager
import sk.magiksoft.sodalis.core.factory.IconFactory

class RemoveEventAction extends MessageAction(null, IconFactory.getInstance.getIcon("remove")){
  private val events = new ListBuffer[Event]

  def getActionMessage(objects: List[_]) = {
    events.clear
    for (obj <- objects if obj.isInstanceOf[Event]) {
      events += obj.asInstanceOf[Event]
    }

    objects.size match {
      case 0 => new ActionMessage(false, LocaleManager.getString("noEventSelected"))
      case 1 => new ActionMessage(true, LocaleManager.getString("removeEvent"))
      case _ => new ActionMessage(true, LocaleManager.getString("removeEvents"))
    }
  }

  def actionPerformed(e: ActionEvent) = ISOptionPane.showConfirmDialog(SodalisApplication.get.getMainFrame, events.size match {
    case 1 => LocaleManager.getString("removeEventConfirm")
    case _ => LocaleManager.getString("removeEventsConfirm")
  }, events.size match {
    case 1 => events(0).getEventName
    case _ => LocaleManager.getString("events")
  }, JOptionPane.YES_NO_OPTION) match {
    case JOptionPane.NO_OPTION =>
    case JOptionPane.YES_OPTION => for (event <- events) {
      EventDataManager.getInstance.removeEvent(event)
    }
  }
}