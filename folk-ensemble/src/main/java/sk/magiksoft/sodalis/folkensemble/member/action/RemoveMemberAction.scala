/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.folkensemble.member.action

import sk.magiksoft.sodalis.folkensemble.member.entity.MemberData
import sk.magiksoft.sodalis.folkensemble.member.entity.MemberData.MemberStatus

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 3/28/11
 * Time: 8:50 PM
 * To change this template use File | Settings | File Templates.
 */

class RemoveMemberAction extends RemovePersonAbstractAction {
  protected def setupDeletedPerson(person: Person) {
    person.getPersonData(classOf[MemberData]).setStatus(MemberStatus.NON_ACTIVE)
  }

  protected def filterPersons = _ => true

  protected def getDeleteConfirmMultipleSelectionMessage = LocaleManager.getString("removeMembersConfirm")

  protected def getDeleteConfirmSingleSelectionMessage = LocaleManager.getString("removeMemberConfirm")

  protected def getMultipleSelectionMessage = LocaleManager.getString("removeMembers")

  protected def getSingleSelectionMessage = LocaleManager.getString("removeMember")

  protected def getNoSelectionMessage = LocaleManager.getString("nothingSelected")
}