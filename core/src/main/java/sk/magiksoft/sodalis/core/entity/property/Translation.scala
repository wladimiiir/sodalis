package sk.magiksoft.sodalis.core.entity.property

import sk.magiksoft.sodalis.core.entity.Entity

/**
 * @author wladimiiir
 * @since 2010/10/15
 */

class Translation[A <: Entity](val key: String, val name: String, value: Option[A] => Option[Any], val valueClass: Class[_] = classOf[String]) {
  def getValue(entity: Option[A] = None) = value.apply(entity)

  override def toString = name
}
