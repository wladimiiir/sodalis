/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.entity

import collection.mutable.ListBuffer
import java.util.{List => jList}
import collection.JavaConversions._
import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import scala.beans.BeanProperty

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/13/11
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */

class BlotSigning extends AbstractDatabaseEntity {
  @BeanProperty var rorschachBlot = new RorschachBlot
  @BeanProperty var reactionTime = 0
  @BeanProperty var blotTime = 0
  var answers = new ListBuffer[BlotAnswer]

  def getAnswers = bufferAsJavaList(answers)

  def setAnswers(jAnswers: jList[BlotAnswer]) {
    answers = new ListBuffer[BlotAnswer]
    answers ++= asScalaBuffer(jAnswers)
  }

  def updateFrom(entity: DatabaseEntity) {
    entity match {
      case signing: BlotSigning if signing ne this => {
        rorschachBlot.updateFrom(signing.rorschachBlot)
        reactionTime = signing.reactionTime
        blotTime = signing.blotTime
        answers = signing.answers
      }
      case _ =>
    }
  }
}