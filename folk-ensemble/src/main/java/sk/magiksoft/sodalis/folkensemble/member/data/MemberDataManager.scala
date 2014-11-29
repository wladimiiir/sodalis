package sk.magiksoft.sodalis.folkensemble.member.data

import sk.magiksoft.sodalis.folkensemble.member.entity.MemberData
import java.util.{List => jList}
import sk.magiksoft.sodalis.person.entity.Person
import sk.magiksoft.sodalis.core.data.remote.client.ClientDataManager

/**
 * @author wladimiiir
 * @since 2011/3/28
 */

object MemberDataManager extends ClientDataManager {
  def getAllMembers(deleted: Boolean): jList[Person] =
    getHQLQueryList("from " + classOf[Person].getName + " as m" + (if (deleted) "" else " where m.deleted=false")).asInstanceOf[jList[Person]]

  def getActiveMembers: jList[Person] =
    getHQLQueryList("select p from " + classOf[Person].getName + " as p, " + classOf[MemberData].getName + " as md " +
      "where md in elements(p.personDatas) and md.status=" + MemberData.MemberStatus.ACTIVE.ordinal).asInstanceOf[jList[Person]]
}
