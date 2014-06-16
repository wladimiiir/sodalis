/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.person.data

import sk.magiksoft.sodalis.category.entity.{EntityDynamicCategory, Categorized}
import sk.magiksoft.sodalis.person.entity.PersonWrapper

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 4/22/11
 * Time: 10:03 AM
 * To change this template use File | Settings | File Templates.
 */

abstract class PersonWrapperDynamicCategory[C <: Categorized](name: String, query: String)
  extends EntityDynamicCategory[PersonWrapper, C](name, query) {

  protected override def getEntityString(entity: PersonWrapper) = entity.getPersonName

  protected override def getWrappedEntity(entity: PersonWrapper) = entity.getPerson
}