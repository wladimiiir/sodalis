
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.folkensemble.member.entity.property

import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslator
import sk.magiksoft.sodalis.person.entity.Person
import sk.magiksoft.sodalis.folkensemble.member.entity.{UniversityData, EnsembleData, MemberData}

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Oct 17, 2010
 * Time: 11:34:42 AM
 * To change this template use File | Settings | File Templates.
 */

class UniversityDataPropertyTranslator extends EntityPropertyTranslator[Person] {
  def getTranslations = List(
    EntityTranslation("university", p => p.getPersonData(classOf[UniversityData]) match {
      case data:UniversityData => Option(data.getUniversity)
      case _ => None
    }),
    EntityTranslation("faculty", p => p.getPersonData(classOf[UniversityData]) match {
      case data: UniversityData => Option(data.getFaculty)
      case _ => None
    }),
    EntityTranslation("department", p => p.getPersonData(classOf[UniversityData]) match {
      case data: UniversityData => Option(data.getDepartment)
      case _ => None
    }),
    EntityTranslation("specialization", p => p.getPersonData(classOf[UniversityData]) match {
      case data: UniversityData => Option(data.getSpecialization)
      case _ => None
    }),
    EntityTranslation("studiumType", p => p.getPersonData(classOf[UniversityData]) match {
      case data: UniversityData => Option(data.getStudiumType)
      case _ => None
    }),
    EntityTranslation("year", p => p.getPersonData(classOf[UniversityData]) match {
      case data: UniversityData => Option(data.getYear)
      case _ => None
    })
    )
}