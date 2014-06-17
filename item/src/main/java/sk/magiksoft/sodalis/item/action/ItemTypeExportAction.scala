
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.item.action

import sk.magiksoft.sodalis.item.ui.DefaultItemContext
import java.util.{ArrayList, List}
import sk.magiksoft.sodalis.core.action.AbstractExportAction

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jul 22, 2010
 * Time: 1:49:29 PM
 * To change this template use File | Settings | File Templates.
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