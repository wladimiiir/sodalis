package sk.magiksoft.sodalis.service.action

import java.awt.event.ActionEvent
import java.util.List
import sk.magiksoft.sodalis.service.entity.Service
import collection.JavaConversions._
import collection.mutable.{Buffer, ListBuffer}
import sk.magiksoft.sodalis.core.action.{MessageAction, ActionMessage}
import sk.magiksoft.sodalis.core.SodalisApplication
import javax.swing.JOptionPane
import sk.magiksoft.sodalis.service.data.ServiceDataManager
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.ui.ISOptionPane
import sk.magiksoft.sodalis.core.factory.IconFactory
import java.util

/**
 * @author wladimiiir
 * @since 2011/3/10
 */

class RemoveServiceAction extends MessageAction(IconFactory.getInstance.getIcon("remove")) {
  private val acts = new ListBuffer[Service]

  def getActionMessage(objects: util.List[_]) = {
    acts.clear()
    acts ++= objects.filter {
      _.isInstanceOf[Service]
    }.map {
      _.asInstanceOf[Service]
    }
    acts.size match {
      case 0 => new ActionMessage(false, LocaleManager.getString("noServicesSelected"))
      case 1 => new ActionMessage(true, LocaleManager.getString("removeService"))
      case _ => new ActionMessage(true, LocaleManager.getString("removeServices"))
    }
  }

  def actionPerformed(e: ActionEvent) {
    val result = acts.size match {
      case 1 => ISOptionPane.showConfirmDialog(
        SodalisApplication.get.getMainFrame,
        LocaleManager.getString("removeServiceConfirm"),
        acts(0).name,
        JOptionPane.YES_NO_OPTION
      )
      case _ => ISOptionPane.showConfirmDialog(
        SodalisApplication.get.getMainFrame,
        LocaleManager.getString("removeServicesConfirm"),
        LocaleManager.getString("removeServices"),
        JOptionPane.YES_NO_OPTION
      )
    }
    result match {
      case JOptionPane.YES_OPTION => {
        acts.foreach(ServiceDataManager.removeDatabaseEntity(_))
      }
      case _ =>
    }
  }
}
