package sk.magiksoft.sodalis.item.entity.property

import sk.magiksoft.sodalis.item.entity.{Item, ItemType}
import sk.magiksoft.sodalis.core.entity.property.{Translation, EntityPropertyTranslator}
import scala.collection.mutable.ListBuffer
import sk.magiksoft.utils.StringUtils

/**
 * @author wladimiiir
 * @since 2010/10/26
 */

class ItemTypeEntityPropertyTranslator(itemType: ItemType) extends EntityPropertyTranslator[Item] {
  def getTranslations = {
    val translations = new ListBuffer[Translation[Item]]

    for (property <- itemType.itemProperties if property.tableColumn) {
      translations += new Translation[Item](StringUtils.removeDiacritics(property.typeName + "." + property.name),
        property.name, optionItem => optionItem match {
          case Some(item) => Option(property.getValue(item))
          case None => None
        })
    }

    translations.toList
  }
}
