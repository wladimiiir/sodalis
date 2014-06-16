
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


/*
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 2/16/11
 * Time: 5:56 PM
 */
package sk.magiksoft.sodalis.event.action

import java.util.List
import sk.magiksoft.sodalis.core.action.ActionMessage
import collection.JavaConversions._
import sk.magiksoft.sodalis.event.entity.Event
import sk.magiksoft.sodalis.event.data.EventDataManager

class EventImportAction extends AbstractImportAction {
  def getActionMessage(objects: List[_]) = new ActionMessage(true, LocaleManager.getString("importEvents"))

  def importObjects(objects: List[_]) = {
    val events = objects.filter({
      _.isInstanceOf[Event]
    }).map {
      _.asInstanceOf[Event]
    }

    events.foreach(e => EventDataManager.getInstance.addDatabaseEntity(e))
    SodalisApplication.get.showMessage(LocaleManager.getString("eventsImported"), new Integer(events.size))
  }
}