package sk.magiksoft.sodalis.item.action

import sk.magiksoft.sodalis.item.ui.DefaultItemContext
import java.util.{ArrayList, List}
import sk.magiksoft.sodalis.core.action.AbstractExportAction

/**
 * @author wladimiiir
 * @since 2010/7/22
 */

class ItemTypeExportAction(val itemContext: DefaultItemContext) extends AbstractExportAction {
  def getActionMessage(objects: List[_]) = null

  def getExportItems(exportType: Int): List[_ <: java.lang.Object] = itemContext.itemDefinitionPanel match {
    case Some(panel) => exportType match {
      case AbstractExportAction.EXPORT_TYPE_ALL => panel.getItemTypes
      case AbstractExportAction.EXPORT_TYPE_SELECTED => panel.getSelectedItemTypes
    }
    case None => new ArrayList[Nothing](0)
  }
}
