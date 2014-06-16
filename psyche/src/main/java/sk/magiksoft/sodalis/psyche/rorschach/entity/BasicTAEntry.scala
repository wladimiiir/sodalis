/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.entity

import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/22/11
 * Time: 9:30 AM
 * To change this template use File | Settings | File Templates.
 */

class BasicTAEntry extends AbstractDatabaseEntity {
  @BeanProperty var answerCount = ""
  @BeanProperty var entry = ""
  @BeanProperty var notMarked = ""
  @BeanProperty var inBrackets = ""
  @BeanProperty var marked = ""
  @BeanProperty var underlined = ""
  @BeanProperty var doubleUnderlined = ""

  def updateFrom(entity: DatabaseEntity) {
    entity match {
      case entry: BasicTAEntry => {
        answerCount = entry.answerCount
        this.entry = entry.entry
        notMarked = entry.notMarked
        inBrackets = entry.inBrackets
        marked = entry.marked
        underlined = entry.underlined
        doubleUnderlined = entry.doubleUnderlined
      }
      case _ =>
    }
  }
}