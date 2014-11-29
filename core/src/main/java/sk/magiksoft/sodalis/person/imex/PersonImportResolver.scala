package sk.magiksoft.sodalis.person.imex

import sk.magiksoft.sodalis.core.imex.{ImExManager, ImportProcessor}
import sk.magiksoft.sodalis.core.data.DefaultDataManager
import sk.magiksoft.sodalis.person.entity.{PrivatePersonData, Person}
import java.sql.Timestamp
import collection.mutable.ListBuffer
import sk.magiksoft.sodalis.category.entity.Category
import scala.collection.JavaConversions

/**
 * @author wladimiiir
 * @since 2010/11/21
 */

class PersonImportResolver extends ImportProcessor[Person] {
  def findSimilarEntity(entity: Person) = DefaultDataManager.getInstance.getDatabaseEntity("select p from Person p, PrivatePersonData ppd where " +
    "ppd in elements(p.personDatas) and titles='" + entity.getTitles + "' and firstName='" + entity.getFirstName
    + "' and lastName='" + entity.getLastName + "' and ppd.birthDate='"
    + new Timestamp(entity.getPersonData(classOf[PrivatePersonData]).getBirthDate.getTimeInMillis) + "'") match {
    case person: Person => person
    case _ => null
  }


  def processImport(entity: Person) = {
    findSimilarEntity(entity) match {
      case person: Person => {
        entity.setCategories(person.getCategories)
        entity.setId(person.getId)
        entity.setPersonDatas(person.getPersonDatas)
        DefaultDataManager.getInstance.updateDatabaseEntity(entity)
        entity
      }
      case _ => {
        val categories = new ListBuffer[Category]
        for (category <- JavaConversions.asScalaBuffer(entity.getCategories)) {
          categories += ImExManager.processEntity(category)
        }
        entity.setCategories(JavaConversions.bufferAsJavaList(categories))
        DefaultDataManager.getInstance.addDatabaseEntity(entity)
      }
    }
  }
}
