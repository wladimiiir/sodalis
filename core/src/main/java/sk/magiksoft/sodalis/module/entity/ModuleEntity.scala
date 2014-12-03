package sk.magiksoft.sodalis.module.entity

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}

import scala.beans.BeanProperty

/**
 * @author wladimiiir 
 * @since 2014/12/02
 */
class ModuleEntity extends AbstractDatabaseEntity {
  @BeanProperty var order: Int = -1
  @BeanProperty var className: String = null
  @BeanProperty var enabled: Boolean = true

  override def updateFrom(entity: DatabaseEntity): Unit = entity match {
    case module: ModuleEntity =>
      order = module.order
      className = module.className
      enabled = module.enabled

    case _ =>
  }
}
