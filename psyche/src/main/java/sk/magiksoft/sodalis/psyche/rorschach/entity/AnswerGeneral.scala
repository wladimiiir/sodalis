/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.entity

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import reflect.BeanProperty

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/20/11
 * Time: 5:57 PM
 * To change this template use File | Settings | File Templates.
 */

class AnswerGeneral extends AbstractDatabaseEntity {
  def updateFrom(entity: DatabaseEntity) {
    entity match {
      case answer:AnswerGeneral if answer ne this => {

      }
      case _ =>
    }
  }
}