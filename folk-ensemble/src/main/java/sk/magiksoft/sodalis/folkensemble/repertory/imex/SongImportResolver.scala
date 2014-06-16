
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.folkensemble.repertory.imex

import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song
import sk.magiksoft.sodalis.core.imex.ImExManager
import scala.collection.JavaConversions._
import sk.magiksoft.sodalis.folkensemble.repertory.data.RepertoryDataManager

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 11/21/10
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
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
        for (category <- asBuffer(entity.getCategories)) {
          categories += ImExManager.processEntity(category)
        }
        entity.setCategories(asList(categories))

        val composers = new ListBuffer[PersonWrapper]
        for (composer <- asBuffer(entity.getComposers)) {
          composers += ImExManager.processEntity(composer)
        }
        val choreographers = new ListBuffer[PersonWrapper]
        for (choreographer <- asBuffer(entity.getChoreographers)) {
          choreographers += ImExManager.processEntity(choreographer)
        }
        val interpreters = new ListBuffer[PersonWrapper]
        for (interpreter <- asBuffer(entity.getInterpreters)) {
          interpreters += ImExManager.processEntity(interpreter)
        }
        val pedagogists = new ListBuffer[PersonWrapper]
        for (pedagogist <- asBuffer(entity.getPedagogists)) {
          pedagogists += ImExManager.processEntity(pedagogist)
        }

        entity.getComposers.clear
        entity.getChoreographers.clear
        entity.getInterpreters.clear
        entity.getPedagogists.clear
        val song = RepertoryDataManager.getInstance.addDatabaseEntity(entity)
        song.setComposers(asList(composers))
        song.setChoreographers(asList(choreographers))
        song.setInterpreters(asList(interpreters))
        song.setPedagogists(asList(pedagogists))
        RepertoryDataManager.getInstance.updateSong(song)
        song
      }
    }
  }
}