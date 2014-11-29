package sk.magiksoft.sodalis.service.entity

import scala.beans.BeanProperty
import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import sk.magiksoft.sodalis.category.entity.{HistorizableMixin, CategorizedMixin}
import sk.magiksoft.sodalis.common.entity.Price

/**
 * @author wladimiiir
 * @since 2011/3/10
 */

class Service extends AbstractDatabaseEntity with CategorizedMixin with HistorizableMixin {
  @BeanProperty var name = ""
  @BeanProperty var code = ""
  @BeanProperty var description = ""
  @BeanProperty var price = new Price

  def updateFrom(entity: DatabaseEntity) {
    entity match {
      case service: Service if service ne this =>
        name = service.name
        code = service.code
        description = service.description
        price = service.price
      case _ =>
    }
  }
}
