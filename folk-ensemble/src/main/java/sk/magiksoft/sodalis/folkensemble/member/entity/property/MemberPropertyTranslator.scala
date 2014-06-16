
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.folkensemble.member.entity.property

import sk.magiksoft.sodalis.person.entity.Person
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslator
import sk.magiksoft.sodalis.folkensemble.member.entity.{EnsembleData, MemberData}
import sk.magiksoft.sodalis.person.entity.property.PersonPropertyTranslator

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Oct 17, 2010
 * Time: 11:34:42 AM
 * To change this template use File | Settings | File Templates.
 */

class MemberPropertyTranslator extends PersonPropertyTranslator {
  override def getTranslations = super.getTranslations ++ List(
    EntityTranslation("memberID", LocaleManager.getString("memberID"),
      p => p.getPersonData(classOf[MemberData]) match {
        case md: MemberData => Option(md.getMemberID)
        case _ => None
      }),
    EntityTranslation("memberStatus", LocaleManager.getString("memberStatus"),
      p => p.getPersonData(classOf[MemberData]) match {
        case md: MemberData => Option(md.getStatus.toString)
        case _ => None
      }),
    EntityTranslation("ensembleGroup", LocaleManager.getString("EnsembleGroup.name"),
      p => p.getPersonData(classOf[EnsembleData]) match {
        case ed: EnsembleData => Option(ed.getEnsembleGroup.getGroupTypeToString)
        case _ => None
      })
  )
}