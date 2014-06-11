
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.core.action

import javax.swing.{Action, AbstractAction}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import java.util.List
import collection.JavaConversions._
import sk.magiksoft.sodalis.core.data.DefaultDataManager
import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.core.entity.DatabaseEntity

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 12/16/10
 * Time: 4:48 PM
 * To change this template use File | Settings | File Templates.
 */

class DefaultImportAction(entityClass:Class[_ <: DatabaseEntity]) extends AbstractImportAction {
  putValue(Action.SHORT_DESCRIPTION, LocaleManager.getString("import"))

  def getActionMessage(objects: List[_]) = new ActionMessage(true, LocaleManager.getString("import"))

  def importObjects(objects: List[_]) {
    objects match {
      case objects: List[AnyRef] => {
        val entities = DefaultDataManager.getInstance.addOrUpdateEntities(objects.filter(o => o.getClass == entityClass).map {
          _.asInstanceOf[DatabaseEntity]
        }.toList).toList
        SodalisApplication.get.showMessage(LocaleManager.getString("entitiesImported"), new Integer(entities.size))
      }
      case _ =>
    }
  }
}