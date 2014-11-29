package sk.magiksoft.sodalis.folkensemble.programme.imex

import sk.magiksoft.sodalis.folkensemble.programme.entity.Programme
import sk.magiksoft.sodalis.folkensemble.programme.data.ProgrammeDataManager
import sk.magiksoft.sodalis.core.imex.{ImportProcessor, ImExManager}
import sk.magiksoft.sodalis.person.entity.PersonWrapper
import scala.collection.mutable.ListBuffer
import sk.magiksoft.sodalis.category.entity.Category
import scala.collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2010/11/21
 */

class ProgrammeImportResolver extends ImportProcessor[Programme] {
  def findSimilarEntity(entity: Programme) = ProgrammeDataManager.getInstance.getDatabaseEntity(classOf[Programme],
    "name='" + entity.getName + "'")

  def processImport(entity: Programme) = findSimilarEntity(entity) match {
    case programme: Programme => {
      programme
    }
    case _ => {
      val categories = new ListBuffer[Category]
      for (category <- entity.getCategories) {
        categories += ImExManager.processEntity(category)
      }
      entity.setCategories(categories)

      val composers = new ListBuffer[PersonWrapper]
      for (composer <- entity.getComposers) {
        composers += ImExManager.processEntity(composer)
      }
      val choreographers = new ListBuffer[PersonWrapper]
      for (choreographer <- entity.getChoreographers) {
        choreographers += ImExManager.processEntity(choreographer)
      }
      val authors = new ListBuffer[PersonWrapper]
      for (author <- entity.getAuthors) {
        authors += ImExManager.processEntity(author)
      }

      entity.getComposers.clear
      entity.getChoreographers.clear
      entity.getInterpreters.clear
      entity.getAuthors.clear
      val programme = ProgrammeDataManager.getInstance.addDatabaseEntity(entity)
      programme.setComposers(composers)
      programme.setChoreographers(choreographers)
      programme.setAuthors(authors)
      ProgrammeDataManager.getInstance.updateProgramme(programme)
      programme
    }
  }
}
