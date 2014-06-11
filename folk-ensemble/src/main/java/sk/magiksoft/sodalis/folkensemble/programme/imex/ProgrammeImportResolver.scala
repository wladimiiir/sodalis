
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.folkensemble.programme.imex

import sk.magiksoft.sodalis.folkensemble.programme.entity.Programme
import sk.magiksoft.sodalis.folkensemble.programme.data.ProgrammeDataManager
import sk.magiksoft.sodalis.category.entity.Category
import collection.mutable.ListBuffer
import sk.magiksoft.sodalis.person.entity.PersonWrapper
import sk.magiksoft.sodalis.core.imex.{ImExManager, ImportProcessor}
import collection.JavaConversions._

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 11/21/10
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */

class ProgrammeImportResolver extends ImportProcessor[Programme] {
  def findSimilarEntity(entity: Programme) = ProgrammeDataManager.getInstance.getDatabaseEntity(classOf[Programme],
    "name='"+entity.getName+"'")

  def processImport(entity: Programme) = findSimilarEntity(entity) match {
    case programme:Programme => {
      programme
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
      val authors = new ListBuffer[PersonWrapper]
      for (author <- asBuffer(entity.getAuthors)) {
        authors += ImExManager.processEntity(author)
      }

      entity.getComposers.clear
      entity.getChoreographers.clear
      entity.getInterpreters.clear
      entity.getAuthors.clear
      val programme = ProgrammeDataManager.getInstance.addDatabaseEntity(entity)
      programme.setComposers(asList(composers))
      programme.setChoreographers(asList(choreographers))
      programme.setAuthors(asList(authors))
      ProgrammeDataManager.getInstance.updateProgramme(programme)
      programme
    }
  }
}