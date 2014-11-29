package sk.magiksoft.sodalis.psyche.rorschach.entity

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, ImageEntity, AbstractDatabaseEntity}
import scala.beans.BeanProperty

/**
 * @author wladimiiir
 * @since 2011/5/13
 */

class RorschachBlot extends AbstractDatabaseEntity {
  @BeanProperty var image = new ImageEntity()
  @BeanProperty var index = 0

  def updateFrom(entity: DatabaseEntity) {
    entity match {
      case blot: RorschachBlot if blot ne this => {
        image.updateFrom(blot.image)
        index = blot.index
      }
      case _ =>
    }
  }
}
