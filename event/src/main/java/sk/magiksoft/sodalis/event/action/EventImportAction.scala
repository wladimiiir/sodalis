package sk.magiksoft.sodalis.event.action

import java.util.List
import sk.magiksoft.sodalis.core.action.{AbstractImportAction, ActionMessage}
import collection.JavaConversions._
import sk.magiksoft.sodalis.event.entity.Event
import sk.magiksoft.sodalis.event.data.EventDataManager
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.SodalisApplication

 /**
  * @author wladimiiir
  * @since 2011/2/16
  */
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
