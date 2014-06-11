
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.core.action

import java.util.List
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.context.{Context, ContextManager}

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 12/16/10
 * Time: 5:01 PM
 * To change this template use File | Settings | File Templates.
 */

class DefaultExportAction(context:Context) extends AbstractExportAction {
  def getActionMessage(objects: List[_]) = new ActionMessage(true, LocaleManager.getString("export"))

  def getExportItems(exportType: Int):List[_ <: Object] = exportType match {
    case AbstractExportAction.EXPORT_TYPE_ALL => context.getEntities
    case AbstractExportAction.EXPORT_TYPE_SELECTED => context.getSelectedEntities
  }
}