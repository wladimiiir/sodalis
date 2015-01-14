package sk.magiksoft.sodalis.module.entity

import javax.persistence.{Column, Entity}

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import sk.magiksoft.sodalis.core.module.Module

import scala.beans.BeanProperty

/**
 * @author wladimiiir 
 * @since 2014/12/02
 */
@Entity
class ModuleEntity extends AbstractDatabaseEntity {
  @BeanProperty @Column var order: Int = -1
  @BeanProperty @Column var className: String = null
  @BeanProperty @Column var enabled: Boolean = true

  private var module: Option[Module] = None

  def getModule: Module = getModule(ClassLoader.getSystemClassLoader)

  def getModule(classLoader: ClassLoader): Module = module match {
    case Some(m) => m
    case None =>
      val m = classLoader.loadClass(className).newInstance().asInstanceOf[Module]
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
