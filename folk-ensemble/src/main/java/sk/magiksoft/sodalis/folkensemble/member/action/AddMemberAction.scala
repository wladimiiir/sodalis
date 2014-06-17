/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.folkensemble.member.action

import sk.magiksoft.sodalis.person.action.AddPersonAbstractAction
import sk.magiksoft.sodalis.folkensemble.member.ui.MemberControlPanel
import sk.magiksoft.sodalis.core.locale.LocaleManager
import scala.collection.JavaConversions._
import sk.magiksoft.sodalis.person.ui.AbstractPersonContext

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 3/28/11
 * Time: 7:29 PM
 * To change this template use File | Settings | File Templates.
 */

class AddMemberAction(personContext: AbstractPersonContext) extends AddPersonAbstractAction(personContext: AbstractPersonContext) {
  protected def createInfoPanels = new MemberControlPanel().getAllInfoPanels.filter {
    _.isWizardSupported
  }.toArray

  protected def getWizardTitle = LocaleManager.getString("newMember")

  protected def getActionName = LocaleManager.getString("addMember")
}