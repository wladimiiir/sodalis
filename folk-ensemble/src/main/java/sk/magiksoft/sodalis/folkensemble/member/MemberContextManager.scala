package sk.magiksoft.sodalis.folkensemble.member

import action.{AddMemberAction, RemoveMemberAction}
import data.MemberDataManager
import entity.MemberData
import entity.MemberData.MemberStatus
import ui.MemberContext
import java.util.List
import sk.magiksoft.sodalis.person.action.SendEmailAction
import sk.magiksoft.sodalis.person.entity.Person
import sk.magiksoft.sodalis.core.utils.Utils
import sk.magiksoft.sodalis.core.context.AbstractContextManager
import sk.magiksoft.sodalis.core.entity.DatabaseEntity
import sk.magiksoft.sodalis.core.registry.RegistryManager

/**
 * @author wladimiiir
 * @since 2011/3/28
 */

object MemberContextManager extends AbstractContextManager {
  def getDataManager = MemberDataManager

  def getDefaultQuery =
    "select p from " + classOf[Person].getName + " p left join p.personDatas as pd " +
      "where pd.class=" + classOf[MemberData].getName + " and pd.status=" + MemberStatus.ACTIVE.ordinal

  override def getFilterConfigFileURL = Utils.getURL("file:data/filter/MemberColumnComponents.xml")

  def createContext = new MemberContext

  def isFullTextActive = true

  override def initPopupActions {
    RegistryManager.registerPopupAction(classOf[Person], new AddMemberAction(getContext.asInstanceOf[MemberContext]))
    RegistryManager.registerPopupAction(classOf[Person], new RemoveMemberAction)
    RegistryManager.registerPopupAction(classOf[Person], new SendEmailAction)
  }

  override def entitiesAdded(entities: List[_ <: DatabaseEntity]) {
    contextInitialized match {
      case false =>
      case true => getContext.asInstanceOf[MemberContext].entitiesAdded(entities)
    }
  }
}
