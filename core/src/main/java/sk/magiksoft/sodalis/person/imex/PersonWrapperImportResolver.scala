package sk.magiksoft.sodalis.person.imex

import sk.magiksoft.sodalis.person.entity.{Person, PersonWrapper}
import sk.magiksoft.sodalis.core.imex.{ImExManager, ImportProcessor}
import sk.magiksoft.sodalis.core.data.DefaultDataManager

/**
 * @author wladimiiir
 * @since 2010/11/21
 */

class PersonWrapperImportResolver extends ImportProcessor[PersonWrapper] {
  def findSimilarEntity(entity: PersonWrapper) = entity.getPerson match {
    case person: Person => DefaultDataManager.getInstance.getDatabaseEntity(classOf[PersonWrapper],
      "person.id=" + person.getId)
    case _ => DefaultDataManager.getInstance.getDatabaseEntity(classOf[PersonWrapper],
      "personName='" + entity.getPersonName + "'")
  }

  def processImport(entity: PersonWrapper) = {
    entity.getPerson match {
      case person: Person => {
        entity.setPerson(ImExManager.processEntity(person))
      }
      case _ =>
    }
    findSimilarEntity(entity) match {
      case wrapper: PersonWrapper => {
        wrapper
      }
      case _ => {
        DefaultDataManager.getInstance.addDatabaseEntity(entity)
      }
    }
  }
}
