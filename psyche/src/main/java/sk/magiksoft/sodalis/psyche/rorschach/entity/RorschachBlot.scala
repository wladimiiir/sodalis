/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.entity

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, ImageEntity, AbstractDatabaseEntity}
import scala.beans.BeanProperty

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/13/11
 * Time: 4:37 PM
 * To change this template use File | Settings | File Templates.
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