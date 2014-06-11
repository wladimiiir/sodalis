
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 1/26/11
 * Time: 7:29 PM
 */
package sk.magiksoft.sodalis.folkensemble.programme.report

import sk.magiksoft.sodalis.core.printing.PrintDocument
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.entity.Entity
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyJRDataSource
import sk.magiksoft.sodalis.folkensemble.programme.entity.Programme
import collection.JavaConversions._

class ProgrammePrintDocument extends PrintDocument(LocaleManager.getString("songList"), "data/reports/programme1.jrxml") {
  protected def getObjectsDataSource(entity: Entity) =
    Option(new EntityPropertyJRDataSource(entity.asInstanceOf[Programme].getProgrammeSongs.map(_.getSong).toList))

  protected def createFieldMap(entity: Entity) = Option(Map(
    "songList" -> LocaleManager.getString("songList"),
    "totalDuration" -> LocaleManager.getString("totalDuration")
  ))

  protected def getFieldDataSources(entity: Entity) = Array(new EntityPropertyJRDataSource(List(entity)))

  protected def initEntity(entity: Entity) = true
}