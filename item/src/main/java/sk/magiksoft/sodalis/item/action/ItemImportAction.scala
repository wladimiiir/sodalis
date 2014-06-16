
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.item.action

import java.util.List
import sk.magiksoft.sodalis.item.ui.DefaultItemContext
import sk.magiksoft.sodalis.item.entity.Item

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