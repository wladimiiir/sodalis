package sk.magiksoft.sodalis.psyche.action

import java.awt.event.ActionEvent
import java.util.List
import collection.mutable.ListBuffer
import sk.magiksoft.sodalis.psyche.entity.PsychoTest
import collection.JavaConversions._
import sk.magiksoft.sodalis.core.action.{MessageAction, ActionMessage}
import sk.magiksoft.sodalis.core.ui.ISOptionPane
import javax.swing.JOptionPane
import sk.magiksoft.sodalis.psyche.data.PsycheDataManager
import sk.magiksoft.sodalis.core.factory.IconFactory
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.SodalisApplication

/**
 * @author wladimiiir
 * @since 2011/6/23
 */

class RemovePsychoTestAction extends MessageAction(IconFactory.getInstance().getIcon("remove")) {

  private val tests = new ListBuffer[PsychoTest]

  def getActionMessage(objects: List[_]) = {
    tests.clear()
    tests ++= objects.filter(_.isInstanceOf[PsychoTest]).map(_.asInstanceOf[PsychoTest])
    tests.size match {
      case 0 => new ActionMessage(false, LocaleManager.getString("noTestSelected"))
      case 1 => new ActionMessage(true, LocaleManager.getString("removeTest"))
      case _ => new ActionMessage(true, LocaleManager.getString("removeTests"))
    }
  }

  def actionPerformed(e: ActionEvent) {
    val result = tests.size match {
      case 1 => ISOptionPane.showConfirmDialog(SodalisApplication.get().getMainFrame, LocaleManager.getString("removeTestConfirm"),
        LocaleManager.getString("removeTest"), JOptionPane.YES_NO_OPTION)
      case _ => ISOptionPane.showConfirmDialog(SodalisApplication.get().getMainFrame, LocaleManager.getString("removeTestsConfirm"),
        LocaleManager.getString("removeTests"), JOptionPane.YES_NO_OPTION)
    }
    result match {
      case JOptionPane.YES_OPTION => {
        for (psychoTest <- tests) {
          PsycheDataManager.removeDatabaseEntity(psychoTest)
        }
      }
      case _ =>
    }
  }
}
