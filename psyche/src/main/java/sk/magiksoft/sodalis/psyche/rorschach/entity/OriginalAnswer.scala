package sk.magiksoft.sodalis.psyche.rorschach.entity

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import scala.beans.BeanProperty

/**
 * @author wladimiiir
 * @since 2011/5/17
 */

class OriginalAnswer extends Signing with QualitySignMixin {
  @BeanProperty var blotIndex = 0

  override def updateFrom(entity: DatabaseEntity) {
    super.updateFrom(entity)
    entity match {
      case oa: OriginalAnswer if oa ne this => {
        blotIndex = oa.blotIndex
        qualitySign = oa.qualitySign
      }
      case _ =>
    }
  }
}
