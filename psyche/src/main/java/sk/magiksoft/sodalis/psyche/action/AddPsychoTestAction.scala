package sk.magiksoft.sodalis.psyche.action

import java.awt.event.ActionEvent
import java.util.List

import sk.magiksoft.sodalis.core.action.{ActionMessage, MessageAction}
import sk.magiksoft.sodalis.core.factory.IconFactory
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.psyche.ui.TestCreationDialog

/**
 * @author wladimiiir
 * @since 2011/5/13
 */

class AddPsychoTestAction extends MessageAction(IconFactory.getInstance().getIcon("add")) {
  def getActionMessage(objects: List[_]) = new ActionMessage(true, LocaleManager.getString("addPsychoTest"))

  def actionPerformed(e: ActionEvent) {
    new TestCreationDialog().setVisible(true)
  }
}
