package sk.magiksoft.sodalis.category.entity

import sk.magiksoft.sodalis.core.entity.DatabaseEntity

/**
 * @author wladimiiir
 * @since 2010/11/26
 */

class DefaultEntityDynamicCategory[T <: DatabaseEntity](name: String, query: String) extends EntityDynamicCategory[T, Categorized](name, query) {
  def acceptCategorized(entity: T, categorized: Categorized) = entity.getId == categorized.getId
}
