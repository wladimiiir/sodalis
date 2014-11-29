package sk.magiksoft.sodalis.item.action

import java.util.List
import sk.magiksoft.sodalis.item.ui.DefaultItemContext
import sk.magiksoft.sodalis.item.entity.ItemType
import sk.magiksoft.sodalis.core.action.AbstractImportAction
import scala.collection.mutable.ListBuffer
import sk.magiksoft.sodalis.core.data.DefaultDataManager
import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.core.locale.LocaleManager
import scala.collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2010/7/28
 */

class ItemTypeImportAction(val itemContext: DefaultItemContext) extends AbstractImportAction {
  def getActionMessage(objects: List[_]) = null


  def importObjects(objects: List[_]) = {
    val itemTypes = new ListBuffer[ItemType]

    for (item <- objects if item.isInstanceOf[ItemType]) {
      itemTypes += item.asInstanceOf[ItemType]
    }

    DefaultDataManager.getInstance.addOrUpdateEntities(itemTypes)
    SodalisApplication.get.showMessage(LocaleManager.getString("itemTypesImported"), itemTypes.size.toString)
  }
}
