
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.form.action

import sk.magiksoft.sodalis.core.factory.IconFactory
import java.util.List
import java.awt.event.ActionEvent
import sk.magiksoft.sodalis.form.entity.Form
import collection.JavaConversions._
import sk.magiksoft.sodalis.core.action.{ActionMessage, MessageAction}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import collection.mutable.{Buffer, ListBuffer}
import javax.swing.JOptionPane
import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.core.ui.ISOptionPane
import sk.magiksoft.sodalis.form.FormDataManager

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Aug 9, 2010
 * Time: 9:05:31 PM
 * To change this template use File | Settings | File Templates.
 */

class RemoveFormAction extends MessageAction(null, IconFactory.getInstance.getIcon("remove")) {
  private var forms: ListBuffer[Form] = new ListBuffer[Form]

  def getActionMessage(objects: List[_]) = {
    forms = new ListBuffer[Form] ++ objects.filter(f => f.isInstanceOf[Form]).asInstanceOf[Buffer[Form]]
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