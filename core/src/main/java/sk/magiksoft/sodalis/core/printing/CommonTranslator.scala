package sk.magiksoft.sodalis.core.printing

import sk.magiksoft.sodalis.core.entity.property.{Translation, Translator}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.entity.Entity

/**
 * @author wladimiiir
 * @since 2011/4/10
 */

class CommonTranslator extends Translator[Entity] {
  def getTranslations = List(
    new Translation[Entity]("entityCount", LocaleManager.getString("count"), entity => Option("1")),
    new Translation[Entity]("empty", LocaleManager.getString("empty"), entity => Option(" "))
  )
}
