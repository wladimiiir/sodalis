package sk.magiksoft.sodalis.item.action

import java.util.List
import sk.magiksoft.sodalis.item.ui.DefaultItemContext
import sk.magiksoft.sodalis.item.entity.Item
import scala.collection.mutable.ListBuffer
import sk.magiksoft.sodalis.core.data.DefaultDataManager
import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.action.AbstractImportAction
import scala.collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2010/7/28
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
