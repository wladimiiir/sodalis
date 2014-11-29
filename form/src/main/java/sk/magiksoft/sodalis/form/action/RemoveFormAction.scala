package sk.magiksoft.sodalis.form.action

import java.util.List
import java.awt.event.ActionEvent
import sk.magiksoft.sodalis.form.entity.Form
import sk.magiksoft.sodalis.core.action.{ActionMessage, MessageAction}
import collection.mutable.ListBuffer
import javax.swing.JOptionPane
import sk.magiksoft.sodalis.form.FormDataManager
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.core.ui.ISOptionPane
import sk.magiksoft.sodalis.core.factory.IconFactory
import scala.collection.mutable
import scala.collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2010/8/9
 */

class RemoveFormAction extends MessageAction(null, IconFactory.getInstance.getIcon("remove")) {
  private var forms: ListBuffer[Form] = new ListBuffer[Form]

  def getActionMessage(objects: List[_]) = {
    forms = new ListBuffer[Form] ++ objects.filter(_.isInstanceOf[Form]).asInstanceOf[mutable.Buffer[Form]]
    forms.size match {
      case 0 => new ActionMessage(false, LocaleManager.getString("noFormSelected"))
      case 1 => new ActionMessage(true, LocaleManager.getString("removeForm"))
      case _ => new ActionMessage(true, LocaleManager.getString("removeForms"))
    }
  }

  def actionPerformed(e: ActionEvent) = {
    val option = forms.size match {
      case 0 => JOptionPane.NO_OPTION
      case 1 => ISOptionPane.showConfirmDialog(SodalisApplication.get.getMainFrame, LocaleManager.getString("removeFormConfirm"), forms.head.name, JOptionPane.YES_NO_OPTION)
      case _ => ISOptionPane.showConfirmDialog(SodalisApplication.get.getMainFrame, LocaleManager.getString("removeFormsConfirm"), LocaleManager.getString("documents"), JOptionPane.YES_NO_OPTION)
    }

    if (option == JOptionPane.YES_OPTION) {
      for (form <- forms) {
        FormDataManager.removeDatabaseEntity(form)
      }
    }
  }
}
