package sk.magiksoft.sodalis.psyche.rorschach.entity

import sk.magiksoft.sodalis.core.entity.{AbstractDatabaseEntity, DatabaseEntity}
import scala.beans.BeanProperty

/**
 * @author wladimiiir
 * @since 2011/5/13
 */

class Signing extends AbstractDatabaseEntity {
  @BeanProperty var name = ""
  @BeanProperty var description = ""
  @BeanProperty var interpretation = ""

  def updateFrom(entity: DatabaseEntity) {
    entity match {
      case signing: Signing if signing ne this => {
        name = signing.name
        description = signing.description
        interpretation = signing.interpretation
      }
      case _ =>
    }
  }

}
