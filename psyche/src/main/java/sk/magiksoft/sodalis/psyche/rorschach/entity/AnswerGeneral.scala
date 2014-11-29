package sk.magiksoft.sodalis.psyche.rorschach.entity

import sk.magiksoft.sodalis.core.entity.{AbstractDatabaseEntity, DatabaseEntity}

/**
 * @author wladimiiir
 * @since 2011/5/20
 */

class AnswerGeneral extends AbstractDatabaseEntity {
  def updateFrom(entity: DatabaseEntity) {
    entity match {
      case answer: AnswerGeneral if answer ne this => {

      }
      case _ =>
    }
  }
}
