package sk.magiksoft.sodalis.core.action

import sk.magiksoft.sodalis.core.entity.Entity

/**
 * @author wladimiiir
 * @since 2011/3/28
 */

trait EntityAction[A <: Entity] {
  def isAllowed(entities: List[A]): Boolean

  def getName(entities: List[A]): String

  def apply(entities: List[A]): Unit
}
