/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.folkensemble.member

import action.{AddMemberAction, RemoveMemberAction}
import data.MemberDataManager
import entity.MemberData
import entity.MemberData.MemberStatus
import sk.magiksoft.sodalis.core.context.AbstractContextManager
import sk.magiksoft.sodalis.core.utils.Utils
import sk.magiksoft.sodalis.person.entity.Person
import ui.MemberContext
import java.util.List
import sk.magiksoft.sodalis.core.entity.DatabaseEntity
import collection.mutable.ListBuffer
import collection.JavaConversions._
import sk.magiksoft.sodalis.core.registry.RegistryManager
import sk.magiksoft.sodalis.person.action.SendEmailAction

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 3/28/11
 * Time: 7:47 PM
 * To change this template use File | Settings | File Templates.
 */

object MemberContextManager extends AbstractContextManager{
  def getDataManager = MemberDataManager

  def getDefaultQuery =
    "select p from " + classOf[Person].getName + " p left join p.personDatas as pd " +
            "where pd.class=" + classOf[MemberData].getName + " and pd.status=" + MemberStatus.ACTIVE.ordinal

  override def getFilterConfigFileURL = Utils.getURL("file:data/filter/MemberColumnComponents.xml")

  def createContext = new MemberContext

  def isFullTextActive = true

  override def initPopupActions {
    import RegistryManager._
    registerPopupAction(classOf[Person], new AddMemberAction(getContext.asInstanceOf[MemberContext]))
    registerPopupAction(classOf[Person], new RemoveMemberAction)
    registerPopupAction(classOf[Person], new SendEmailAction)
  }

  override def entitiesAdded(entities: List[_ <: DatabaseEntity]) {
    contextInitialized match {
      case false =>
      case true => getContext.asInstanceOf[MemberContext].entitiesAdded(entities)
    }
  }
}