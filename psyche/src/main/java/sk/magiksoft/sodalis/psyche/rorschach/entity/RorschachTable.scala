/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.entity

import reflect.BeanProperty
import sk.magiksoft.sodalis.core.entity.{ImageEntity, DatabaseEntity, AbstractDatabaseEntity}

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/13/11
 * Time: 4:37 PM
 * To change this template use File | Settings | File Templates.
 */

class RorschachTable extends AbstractDatabaseEntity {
  @BeanProperty var image = new ImageEntity()
  @BeanProperty var index = 0

  def updateFrom(entity: DatabaseEntity) {
    entity match {
      case table:RorschachTable if table ne this => {
        image.updateFrom(table.image)
        index = table.index
      }
      case _ =>
    }
  }
}