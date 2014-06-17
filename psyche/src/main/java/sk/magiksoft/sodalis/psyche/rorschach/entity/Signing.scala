/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.entity

import sk.magiksoft.sodalis.core.entity.{AbstractDatabaseEntity, DatabaseEntity}
import scala.beans.BeanProperty

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/13/11
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
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