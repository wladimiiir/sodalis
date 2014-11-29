package sk.magiksoft.sodalis.core.action

import java.util.List
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.context.Context

/**
 * @author wladimiiir
 * @since 2010/12/16
 */

class DefaultExportAction(context: Context) extends AbstractExportAction {
  def getActionMessage(objects: List[_]) = new ActionMessage(true, LocaleManager.getString("export"))

  def getExportItems(exportType: Int): List[_ <: Object] = exportType match {
    case AbstractExportAction.EXPORT_TYPE_ALL => context.getEntities
    case AbstractExportAction.EXPORT_TYPE_SELECTED => context.getSelectedEntities
  }
}
