/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.folkensemble.member

import action.{AddMemberAction, RemoveMemberAction}
import data.MemberDataManager
import entity.MemberData
import entity.MemberData.MemberStatus
import ui.MemberContext
import java.util.List

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 3/28/11
 * Time: 7:47 PM
 * To change this template use File | Settings | File Templates.
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