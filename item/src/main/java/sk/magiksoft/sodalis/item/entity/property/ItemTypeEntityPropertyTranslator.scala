
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.item.entity.property

import sk.magiksoft.sodalis.item.entity.{Item, ItemType}
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslator

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 10/26/10
 * Time: 10:42 PM
 * To change this template use File | Settings | File Templates.
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