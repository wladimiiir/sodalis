package sk.magiksoft.sodalis.folkensemble.repertory.imex

import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song
import sk.magiksoft.sodalis.core.imex.{ImportProcessor, ImExManager}
import scala.collection.JavaConversions._
import sk.magiksoft.sodalis.folkensemble.repertory.data.RepertoryDataManager
import sk.magiksoft.sodalis.person.entity.PersonWrapper
import scala.collection.mutable.ListBuffer
import sk.magiksoft.sodalis.category.entity.Category
import sk.magiksoft.sodalis.core.data.DefaultDataManager

/**
 * @author wladimiiir
 * @since 2010/11/21
 */

class SongImportResolver extends ImportProcessor[Song] {
  def findSimilarEntity(entity: Song) = DefaultDataManager.getInstance.getDatabaseEntity(classOf[Song],
    "name='" + entity.getName + "' and genre='" + entity.getGenre + "'")

  def processImport(entity: Song) = {
    findSimilarEntity(entity) match {
      case song: Song => {
        entity.setId(song.getId)
        entity.updateFrom(song)
        RepertoryDataManager.getInstance.updateDatabaseEntity(entity)
        entity
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
        val interpreters = new ListBuffer[PersonWrapper]
        for (interpreter <- entity.getInterpreters) {
          interpreters += ImExManager.processEntity(interpreter)
        }
        val pedagogists = new ListBuffer[PersonWrapper]
        for (pedagogist <- entity.getPedagogists) {
          pedagogists += ImExManager.processEntity(pedagogist)
        }

        entity.getComposers.clear
        entity.getChoreographers.clear
        entity.getInterpreters.clear
        entity.getPedagogists.clear
        val song = RepertoryDataManager.getInstance.addDatabaseEntity(entity)
        song.setComposers(composers)
        song.setChoreographers(choreographers)
        song.setInterpreters(interpreters)
        song.setPedagogists(pedagogists)
        RepertoryDataManager.getInstance.updateSong(song)
        song
      }
    }
  }
}
