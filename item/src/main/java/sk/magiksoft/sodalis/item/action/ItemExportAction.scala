package sk.magiksoft.sodalis.item.action

import java.util.List
import sk.magiksoft.sodalis.item.ui.DefaultItemContext
import sk.magiksoft.sodalis.core.action.AbstractExportAction

/**
 * @author wladimiiir
 * @since 2010/7/22
 */

class ItemExportAction(val itemContext: DefaultItemContext) extends AbstractExportAction {
  def getActionMessage(objects: List[_]) = null

  def getExportItems(exportType: Int): List[_ <: java.lang.Object] = exportType match {
    case AbstractExportAction.EXPORT_TYPE_SELECTED => itemContext.getSelectedEntities
    case AbstractExportAction.EXPORT_TYPE_ALL => itemContext.getEntities
  }
}
