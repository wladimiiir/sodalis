package sk.magiksoft.sodalis.core.entity.property

import sk.magiksoft.sodalis.core.entity.Entity

/**
 * @author wladimiiir
 * @since 2010/10/16
 */

trait Translator[A <: Entity] {
  def getTranslations: List[Translation[A]]
}
