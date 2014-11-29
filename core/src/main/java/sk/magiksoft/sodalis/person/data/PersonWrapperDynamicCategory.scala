package sk.magiksoft.sodalis.person.data

import sk.magiksoft.sodalis.category.entity.{EntityDynamicCategory, Categorized}
import sk.magiksoft.sodalis.person.entity.PersonWrapper

/**
 * @author wladimiiir
 * @since 2011/4/22
 */

abstract class PersonWrapperDynamicCategory[C <: Categorized](name: String, query: String)
  extends EntityDynamicCategory[PersonWrapper, C](name, query) {

  protected override def getEntityString(entity: PersonWrapper) = entity.getPersonName

  protected override def getWrappedEntity(entity: PersonWrapper) = entity.getPerson
}
