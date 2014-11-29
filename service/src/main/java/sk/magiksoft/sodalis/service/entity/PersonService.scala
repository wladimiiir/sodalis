package sk.magiksoft.sodalis.service.entity

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import java.util.Calendar
import scala.beans.BeanProperty

/**
 * @author wladimiiir
 * @since 2011/3/17
 */

class PersonService(@BeanProperty var service: Service = null, @BeanProperty var date: Calendar = Calendar.getInstance) extends AbstractDatabaseEntity {
  def updateFrom(entity: DatabaseEntity) {
    entity match {
      case ps: PersonService if ps ne this => {
        service = ps.service
        date = ps.date
      }
      case _ =>
    }
  }
}
