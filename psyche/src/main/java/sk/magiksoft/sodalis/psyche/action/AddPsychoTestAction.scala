/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.action

import java.awt.event.ActionEvent
import java.util.List
import sk.magiksoft.sodalis.psyche.rorschach.ui.{TableSigningDialog}
import sk.magiksoft.sodalis.psyche.rorschach.entity.{RorschachTest, TestResult}
import sk.magiksoft.sodalis.core.action.ActionMessage
import javax.swing.AbstractAction
import sk.magiksoft.sodalis.core.factory.IconFactory
import sk.magiksoft.sodalis.psyche.ui.TestCreationDialog

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/13/11
 * Time: 12:03 PM
 * To change this template use File | Settings | File Templates.
 */

class AddPsychoTestAction extends MessageAction(IconFactory.getInstance().getIcon("add")) {
  def getActionMessage(objects: List[_]) = new ActionMessage(true, LocaleManager.getString("addPsychoTest"))

  def actionPerformed(e: ActionEvent) {
    new TestCreationDialog().setVisible(true)
  }
}