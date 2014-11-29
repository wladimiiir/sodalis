package sk.magiksoft.sodalis.folkensemble.member.action

import sk.magiksoft.sodalis.folkensemble.member.entity.MemberData
import sk.magiksoft.sodalis.folkensemble.member.entity.MemberData.MemberStatus
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.person.entity.Person
import sk.magiksoft.sodalis.person.action.RemovePersonAbstractAction

/**
 * @author wladimiiir
 * @since 2011/3/28
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
