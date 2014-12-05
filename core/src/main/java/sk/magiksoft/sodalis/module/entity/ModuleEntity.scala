package sk.magiksoft.sodalis.module.entity

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import sk.magiksoft.sodalis.core.module.Module

import scala.beans.BeanProperty

/**
 * @author wladimiiir 
 * @since 2014/12/02
 */
class ModuleEntity extends AbstractDatabaseEntity {
  @BeanProperty var order: Int = -1
  @BeanProperty var className: String = null
  @BeanProperty var enabled: Boolean = true

  private var module: Option[Module] = None

  def getModule: Module = module match {
    case Some(m) => m
    case None =>
      val m = Class.forName(className).newInstance().asInstanceOf[Module]
      module = Option(m)
      m
  }

  override def updateFrom(entity: DatabaseEntity): Unit = entity match {
    case module: ModuleEntity =>
      order = module.order
      className = module.className
      enabled = module.enabled

    case _ =>
  }
}
