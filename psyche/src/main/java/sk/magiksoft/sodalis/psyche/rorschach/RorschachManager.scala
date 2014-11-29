package sk.magiksoft.sodalis.psyche.rorschach

import entity._
import collection.mutable.ListBuffer
import sk.magiksoft.sodalis.psyche.data.PsycheDataManager
import scala.collection.mutable

/**
 * @author wladimiiir
 * @since 2011/5/22
 */

object RorschachManager {
  def getAnswers[A](result: TestResult, folding: (BlotAnswer => List[A])): List[A] = {
    result.blotSignings.foldLeft(new ListBuffer[A]) {
      (buffer, signing) => signing.answers.foldLeft(buffer) {
        (buffer, answer) => buffer ++= folding(answer)
      }
    }.toList
  }

  def calculateAperceptionEntryGroupCount(signings: List[BlotSigning]): Map[String, Int] = {
    signings.foldLeft(new mutable.HashMap[String, Int]) {
      (map, signing) => {
        val countMap = calculateAperceptionEntryGroupCount(signing)
        for ((group, count) <- countMap) {
          map.get(group) match {
            case Some(groupCount) => map += group -> (groupCount + count)
            case None => map += group -> count
          }
        }
        map
      }
    }.toMap
  }

  def calculateAperceptionEntryGroupCount(signing: BlotSigning): Map[String, Int] = {
    val aperceptions = signing.answers.foldLeft(new ListBuffer[Aperception]) {
      (aperceptions, answer) => aperceptions ++= answer.aperceptions
        aperceptions
    }
    aperceptions.flatMap(_.taEntryGroups.split(";")).foldLeft(new mutable.HashMap[String, Int]) {
      (map, group) => map.get(group) match {
        case Some(count) => {
          map += group -> (count + 1)
          map
        }
        case None => {
          map += group -> 1
          map
        }
      }
    }.toMap
  }

  def determineAperceptionType(answerCount: Int, aperceptionEntryGroupCountMap: Map[String, Int]) = {
    val CountRange = """(\d+)-(\d+)""".r
    val apercetionTypeEntries = new ListBuffer[String]
    val order = "GDDdzwDo"
    val entries = PsycheDataManager.basicTAEntries.filter(entry => {
      entry.answerCount match {
        case CountRange(from, to) => Range(from.toInt, to.toInt + 1).contains(answerCount)
        case count: String if count.forall(_.isDigit) => count.toInt == answerCount
        case _ => false
      }
    })

    for ((entryGroup, count) <- aperceptionEntryGroupCountMap.toList
      .sortWith((tuple1, tuple2) => order.indexOf(tuple1._1) < order.indexOf(tuple2._1))) {
      entries.find(_.entry == entryGroup) match {
        case Some(taEntry) => {
          if (isCountAccepted(taEntry.inBrackets)) {
            apercetionTypeEntries += '(' + entryGroup + ')'
          } else if (isCountAccepted(taEntry.marked)) {
            apercetionTypeEntries += entryGroup
          } else if (isCountAccepted(taEntry.underlined)) {
            apercetionTypeEntries += entryGroup + '_'
          } else if (isCountAccepted(taEntry.doubleUnderlined)) {
            apercetionTypeEntries += entryGroup + "__"
          }
        }
        case None =>
      }

      def isCountAccepted(countString: String) = countString match {
        case CountRange(from, to) => Range(from.toInt, to.toInt + 1).contains(count)
        case countString: String if countString.forall(_.isDigit) => countString.toInt == count
        case _ => false
      }
    }
    apercetionTypeEntries.toList
  }


  def calculateF1Percent(signings: List[BlotSigning]) = {
    val FAnswerDeterminants = signings.foldLeft(List[AnswerDeterminant]()) {
      (determinants, signing) => signing.answers.foldLeft(determinants) {
        (determinants, answer) => answer.answerDeterminants.filter(_.determinant.name == "F").toList ::: determinants
      }
    }
    FAnswerDeterminants.foldLeft(0.0) {
      (value, determinant) => determinant.qualitySign match {
        case Some(sign) if sign == QualitySign.+ => value + 1
        case Some(sign) if sign == QualitySign.+- => value + 0.5
        case _ => value
      }
    } / (if (FAnswerDeterminants.isEmpty) 1 else FAnswerDeterminants.size)
  }

  def calculateF2Percent(signings: List[BlotSigning]) = {
    val FAnswerDeterminants = signings.foldLeft(List[AnswerDeterminant]()) {
      (determinants, signing) => signing.answers.foldLeft(determinants) {
        (determinants, answer) => answer.answerDeterminants.filter(_.determinant.qualitySign).toList ::: determinants
      }
    }
    FAnswerDeterminants.foldLeft(0.0) {
      (value, determinant) => determinant.qualitySign match {
        case Some(sign) if sign == QualitySign.+ => value + 1
        case Some(sign) if sign == QualitySign.+- => value + 0.5
        case _ => value
      }
    } / (if (FAnswerDeterminants.isEmpty) 1 else FAnswerDeterminants.size)
  }

  def calculateExperientalType(signings: List[BlotSigning]) =
    calculateRatios(signings, Map(
      "B" -> 1.0,
      "Bsec" -> 0.5
    ), Map(
      "Fb" -> 1.5,
      "FbF" -> 1.0,
      "FFb" -> 0.5
    ))

  def calculateSecondaryFormula(signings: List[BlotSigning]) =
    calculateRatios(signings, Map(
      "Bkl" -> 1.0,
      "BT" -> 1.0,
      "BO" -> 1.0,
      "Asoc B" -> 1.0,
      "Abstr B" -> 1.0
    ), Map(
      "Hd" -> 1.5,
      "aFb" -> 1.5,
      "nFb" -> 1.5,
      "HdF" -> 1.0,
      "aFbF" -> 1.0,
      "nFbF" -> 1.0,
      "FHd" -> 0.5,
      "aFFb" -> 0.5,
      "nFFb" -> 0.5,
      "F(Fb)" -> 0.5
    ))

  def calculateRatios(signings: List[BlotSigning], leftPointMap: Map[String, Double], rightPointMap: Map[String, Double]) = {
    val leftSide = signings.foldLeft(0.0) {
      (value, signing) => signing.answers.foldLeft(value) {
        (value, answer) => answer.answerDeterminants.foldLeft(value) {
          (value, ad) => leftPointMap.get(ad.determinant.name) match {
            case Some(points) => value + points
            case None => value
          }
        }
      }
    }
    val rightSide = signings.foldLeft(0.0) {
      (value, signing) => signing.answers.foldLeft(value) {
        (value, answer) => answer.answerDeterminants.foldLeft(value) {
          (value, ad) => rightPointMap.get(ad.determinant.name) match {
            case Some(points) => points + value
            case None => value
          }
        }
      }
    }
    (leftSide, rightSide)
  }

  def calculateAffectiveType(signings: List[BlotSigning]) = {
    val leftSide = signings.foldLeft(0.0) {
      (value, signing) => signing.answers.foldLeft(value) {
        (value, answer) => answer.aperceptions.foldLeft(value) {
          (value, aperception) => aperception.name match {
            case "Gzw" => value + 1
            case "DzwG" => value + 1
            case "Dzw" => value + 1
            case "Ddzw" => value + 1
            case "zw" => value + 1
            case _ => value
          }
        }
      }
    }
    val rightSide = calculateRatios(signings, Map(), Map(
      "Hd" -> 1.5,
      "HdF" -> 1.0,
      "FHd" -> 0.5
    ))._2
    (leftSide, rightSide)
  }


}
