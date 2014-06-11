/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.entity

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import reflect.BeanProperty

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/17/11
 * Time: 7:21 PM
 * To change this template use File | Settings | File Templates.
 */

class OriginalAnswer extends Signing with QualitySignMixin {
  @BeanProperty var tableIndex = 0

  override def updateFrom(entity: DatabaseEntity) {
    super.updateFrom(entity)
    entity match {
      case oa:OriginalAnswer if oa ne this => {
        tableIndex = oa.tableIndex
        qualitySign = oa.qualitySign
      }
      case _ =>
    }
  }
}