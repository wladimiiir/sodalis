
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.category.entity

import sk.magiksoft.sodalis.core.entity.DatabaseEntity

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 11/26/10
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */

class DefaultEntityDynamicCategory[T <: DatabaseEntity](name: String, query: String) extends EntityDynamicCategory[T, Categorized](name, query) {
  def acceptCategorized(entity: T, categorized: Categorized) = entity.getId == categorized.getId
}