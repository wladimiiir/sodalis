/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.entity

import collection.mutable.ListBuffer
import java.util.{List => jList}
import sk.magiksoft.sodalis.category.entity.Category
import collection.JavaConversions._
import reflect.BeanProperty
import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/13/11
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */

class TableSigning extends AbstractDatabaseEntity {
  @BeanProperty var rorschachTable = new RorschachTable
  @BeanProperty var reactionTime = 0
  @BeanProperty var tableTime = 0
  var answers = new ListBuffer[TableAnswer]

  def getAnswers = bufferAsJavaList(answers)

  def setAnswers(jAnswers: jList[TableAnswer]) {
    answers = new ListBuffer[TableAnswer]
    answers ++= asScalaBuffer(jAnswers)
  }

  def updateFrom(entity: DatabaseEntity) {
    entity match {
      case signing:TableSigning if signing ne this=> {
        rorschachTable.updateFrom(signing.rorschachTable)
        reactionTime = signing.reactionTime
        tableTime = signing.tableTime
        answers = signing.answers
      }
      case _ =>
    }
  }
}