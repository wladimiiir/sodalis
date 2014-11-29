package sk.magiksoft.sodalis.event.action

import java.awt.event.ActionEvent
import sk.magiksoft.sodalis.core.action.{ActionMessage, MessageAction}
import sk.magiksoft.sodalis.event.entity.Event
import collection.mutable.ListBuffer
import sk.magiksoft.sodalis.core.SodalisApplication
import javax.swing.JOptionPane
import sk.magiksoft.sodalis.event.data.EventDataManager
import sk.magiksoft.sodalis.core.factory.IconFactory
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.ui.ISOptionPane
import scala.collection.JavaConversions._

 /**
  * @author wladimiiir
  * @since 2011/2/16
  */
class RemoveEventAction extends MessageAction(null, IconFactory.getInstance.getIcon("remove")) {
  private val events = new ListBuffer[Event]

  def getActionMessage(objects: java.util.List[_]) = {
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
