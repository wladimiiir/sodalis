
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.person.imex

import sk.magiksoft.sodalis.core.imex.{ImExManager, ImportProcessor}
import sk.magiksoft.sodalis.core.data.DefaultDataManager
import sk.magiksoft.sodalis.person.entity.{PrivatePersonData, Person}
import java.sql.Timestamp
import scala.collection.JavaConversions._
import collection.mutable.ListBuffer
import sk.magiksoft.sodalis.category.entity.Category

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 11/21/10
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */

class PersonImportResolver extends ImportProcessor[Person] {
  def findSimilarEntity(entity: Person) = DefaultDataManager.getInstance.getDatabaseEntity("select p from Person p, PrivatePersonData ppd where " +
          "ppd in elements(p.personDatas) and titles='" + entity.getTitles + "' and firstName='" + entity.getFirstName
          + "' and lastName='" + entity.getLastName + "' and ppd.birthDate='"
          + new Timestamp(entity.getPersonData(classOf[PrivatePersonData]).getBirthDate.getTimeInMillis) + "'") match {
    case person:Person => person
    case _ => null
  } 


  def processImport(entity: Person) = {
    findSimilarEntity(entity) match {
      case person:Person => {
        entity.setCategories(person.getCategories)
        entity.setId(person.getId)
        entity.setPersonDatas(person.getPersonDatas)
        DefaultDataManager.getInstance.updateDatabaseEntity(entity)
        entity
      }
      case _ => {
        val categories = new ListBuffer[Category]
        for (category <- asBuffer(entity.getCategories)) {
          categories += ImExManager.processEntity(category)
        }
        entity.setCategories(asList(categories))
        DefaultDataManager.getInstance.addDatabaseEntity(entity)
      }
    }
  }
}