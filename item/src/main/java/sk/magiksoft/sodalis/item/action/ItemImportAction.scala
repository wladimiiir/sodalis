
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.item.action

import sk.magiksoft.sodalis.core.action.AbstractImportAction
import java.util.List
import java.awt.event.ActionEvent
import sk.magiksoft.sodalis.item.ui.DefaultItemContext
import collection.JavaConversions._
import sk.magiksoft.sodalis.item.entity.Item
import collection.mutable.ListBuffer
import sk.magiksoft.sodalis.core.utils.Utils
import sk.magiksoft.sodalis.core.data.DefaultDataManager
import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.core.locale.LocaleManager

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jul 28, 2010
 * Time: 8:43:45 PM
 * To change this template use File | Settings | File Templates.
 */

class ItemImportAction(val itemContext: DefaultItemContext) extends AbstractImportAction {
  def getActionMessage(objects: List[_]) = null


  def importObjects(objects: List[_]) = {
    val items = new ListBuffer[Item]

    for (item <- objects if item.isInstanceOf[Item]) {
      items += item.asInstanceOf[Item]
    }

    DefaultDataManager.getInstance.addOrUpdateEntities(items)
    SodalisApplication.get.showMessage(LocaleManager.getString("itemsImported"), items.size.toString)
  }
}