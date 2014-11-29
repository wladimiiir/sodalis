package sk.magiksoft.sodalis.psyche.rorschach.entity

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import collection.mutable.ListBuffer
import java.util.{List => jList}
import collection.mutable.ListBuffer._
import scala.beans.BeanProperty
import scala.collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2011/5/13
 */

class BlotAnswer extends AbstractDatabaseEntity {
  @BeanProperty var answer = ""
  @BeanProperty var myInterpretation = ""
  var aperceptions = new ListBuffer[Aperception]
  var answerDeterminants = new ListBuffer[AnswerDeterminant]
  var contents = new ListBuffer[Content]
  var answerOriginalAnswers = new ListBuffer[AnswerOriginalAnswer]
  var vulgarAnswers = new ListBuffer[VulgarAnswer]
  var specialSigns = new ListBuffer[SpecialSign]

  def getAperceptions = bufferAsJavaList(aperceptions)

  def setAperceptions(jAperceptions: jList[Aperception]) {
    aperceptions = new ListBuffer[Aperception]
    aperceptions ++= asScalaBuffer(jAperceptions)
  }

  def getAnswerDeterminants = bufferAsJavaList(answerDeterminants)

  def setAnswerDeterminants(jAnswerDeterminants: jList[AnswerDeterminant]) {
    answerDeterminants = new ListBuffer[AnswerDeterminant]
    answerDeterminants ++= asScalaBuffer(jAnswerDeterminants)
  }

  def getContents = bufferAsJavaList(contents)

  def setContents(jContents: jList[Content]) {
    contents = new ListBuffer[Content]
    contents ++= asScalaBuffer(jContents)
  }

  def getAnswerOriginalAnswers = bufferAsJavaList(answerOriginalAnswers)

  def setAnswerOriginalAnswers(jAnswerOriginalAnswers: jList[AnswerOriginalAnswer]) {
    answerOriginalAnswers = new ListBuffer[AnswerOriginalAnswer]
    answerOriginalAnswers ++= asScalaBuffer(jAnswerOriginalAnswers)
  }

  def getVulgarAnswers = bufferAsJavaList(vulgarAnswers)

  def setVulgarAnswers(jVulgarAnswers: jList[VulgarAnswer]) {
    vulgarAnswers = new ListBuffer[VulgarAnswer]
    vulgarAnswers ++= asScalaBuffer(jVulgarAnswers)
  }

  def getSpecialSigns = bufferAsJavaList(specialSigns)

  def setSpecialSigns(jSpecialSigns: jList[SpecialSign]) {
    specialSigns = new ListBuffer[SpecialSign]
    specialSigns ++= asScalaBuffer(jSpecialSigns)
  }

  def updateFrom(entity: DatabaseEntity) {
    entity match {
      case answer: BlotAnswer if answer ne this => {
        this.answer = answer.answer
      }
      case _ =>
    }
  }
}
