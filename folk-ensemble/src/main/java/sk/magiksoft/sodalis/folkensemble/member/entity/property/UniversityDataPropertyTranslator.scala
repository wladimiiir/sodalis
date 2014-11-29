package sk.magiksoft.sodalis.folkensemble.member.entity.property

import sk.magiksoft.sodalis.folkensemble.member.entity.UniversityData
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslator
import sk.magiksoft.sodalis.person.entity.Person

/**
 * @author wladimiiir
 * @since 2010/10/17
 */

class UniversityDataPropertyTranslator extends EntityPropertyTranslator[Person] {
  def getTranslations = List(
    EntityTranslation("university", p => p.getPersonData(classOf[UniversityData]) match {
      case data: UniversityData => Option(data.getUniversity)
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
