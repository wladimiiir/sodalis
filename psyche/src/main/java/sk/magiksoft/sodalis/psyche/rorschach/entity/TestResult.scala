package sk.magiksoft.sodalis.psyche.rorschach.entity

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import java.util.{List => jList}
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2011/5/18
 */

class TestResult extends AbstractDatabaseEntity {
  var blotSignings = new ListBuffer[BlotSigning]

  def getBlotSignings = bufferAsJavaList(blotSignings)

  def setBlotSignings(jBlotSignings: jList[BlotSigning]) {
    blotSignings = new ListBuffer[BlotSigning]
    blotSignings ++= jBlotSignings
  }

  def totalAnswerCount = blotSignings.foldLeft(0) {
    (count, signing) => count + signing.answers.size
  }

  def findAnswers[A](folding: (BlotAnswer => List[A])): List[A] = blotSignings.foldLeft(new ListBuffer[A]) {
    (buffer, signing) => signing.answers.foldLeft(buffer) {
      (buffer, answer) => buffer ++= folding(answer)
    }
  }.toList

  def updateFrom(entity: DatabaseEntity) {
    entity match {
      case result: TestResult if result ne this => {
        blotSignings = result.blotSignings
      }
      case _ =>
    }
  }
}
