package sk.magiksoft.sodalis.event.action

import java.util.List
import sk.magiksoft.sodalis.event.EventContextManager
import sk.magiksoft.sodalis.event.data.EventDataManager
import sk.magiksoft.sodalis.event.entity.Event
import sk.magiksoft.sodalis.core.action.{ActionMessage, AbstractExportAction}
import sk.magiksoft.sodalis.core.locale.LocaleManager

 /**
  * @author wladimiiir
  * @since 2011/2/16
  */
class EventExportAction extends AbstractExportAction {
  def getActionMessage(objects: List[_]) = new ActionMessage(true, LocaleManager.getString("exportEvents"))

  def getExportItems(exportType: Int): List[_ <: java.lang.Object] = exportType match {
    case AbstractExportAction.EXPORT_TYPE_ALL => EventDataManager.getInstance.getDatabaseEntities(classOf[Event])
    case AbstractExportAction.EXPORT_TYPE_SELECTED => EventContextManager.getContext.getSelectedEntities
  }
}
