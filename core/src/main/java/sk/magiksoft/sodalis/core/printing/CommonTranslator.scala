/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.core.printing

import sk.magiksoft.sodalis.core.entity.property.{Translation, Translator}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.entity.Entity

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 4/10/11
 * Time: 3:25 PM
 * To change this template use File | Settings | File Templates.
 */

class CommonTranslator extends Translator[Entity]{
  def getTranslations = List(
    new Translation[Entity]("entityCount", LocaleManager.getString("count"), entity => Option("1")),
    new Translation[Entity]("empty", LocaleManager.getString("empty"), entity => Option(" "))
  )
}