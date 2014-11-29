package sk.magiksoft.sodalis.psyche.rorschach.ui.interpretation

import scala.swing.{GridBagPanel, Alignment, Label}
import java.awt.Font
import java.text.DecimalFormat
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.psyche.rorschach.entity.{QualitySign, Vulgarity, TestResult}
import sk.magiksoft.sodalis.psyche.rorschach.event.BlotAnswerRemoved
import sk.magiksoft.sodalis.psyche.ui.LabeledGridBagPanelMixin

/*
* Copyright (c) 2011
*/

/**
 * @author wladimiiir
 * @since 2011/5/22
 */

class AnswerFrequencyInterpretationPanel extends GridBagPanel with LabeledGridBagPanelMixin with InterpretationPanel {
  private val V1Count = createLabel
  private val V2Count = createLabel
  private val V3Count = createLabel
  private val OPlusCount = createLabel
  private val OPlusMinusCount = createLabel
  private val OMinusCount = createLabel
  private val V_% = createLabel
  private val O_% = createLabel
  private val VComparation = createLabel
  private val percentFormat = new DecimalFormat("0.00 %")

  addLabeledComponent(LocaleManager.getString("V1Count"), V1Count)
  addLabeledComponent(LocaleManager.getString("V2Count"), V2Count)
  addLabeledComponent(LocaleManager.getString("V3Count"), V3Count)
  addLabeledComponent(LocaleManager.getString("O+Count"), OPlusCount)
  addLabeledComponent(LocaleManager.getString("O+-Count"), OPlusMinusCount)
  addLabeledComponent(LocaleManager.getString("O-Count"), OMinusCount)
  addLabeledComponent(LocaleManager.getString("V%"), V_%)
  addLabeledComponent(LocaleManager.getString("O%"), O_%)
  addLabeledComponent(" ", new Label)
  addLabeledComponent(" ", VComparation)

  protected def setupValues(testResult: Option[TestResult]) {
    V1Count.text = testResult match {
      case Some(result) => result.findAnswers(answer => answer.vulgarAnswers.filter(_.vulgarity == Vulgarity.V1).toList).size.toString
      case None => ""
    }
    V2Count.text = testResult match {
      case Some(result) => result.findAnswers(answer => answer.vulgarAnswers.filter(_.vulgarity == Vulgarity.V2).toList).size.toString
      case None => ""
    }
    V3Count.text = testResult match {
      case Some(result) => result.findAnswers(answer => answer.vulgarAnswers.filter(_.vulgarity == Vulgarity.V3).toList).size.toString
      case None => ""
    }
    OPlusCount.text = testResult match {
      case Some(result) => result.findAnswers(answer => answer.answerOriginalAnswers.filter(_.qualitySign == Option(QualitySign.+)).toList).size.toString
      case None => ""
    }
    OPlusMinusCount.text = testResult match {
      case Some(result) => result.findAnswers(answer => answer.answerOriginalAnswers.filter(_.qualitySign == Option(QualitySign.+-)).toList).size.toString
      case None => ""
    }
    OMinusCount.text = testResult match {
      case Some(result) => result.findAnswers(answer => answer.answerOriginalAnswers.filter(_.qualitySign == Option(QualitySign.-)).toList).size.toString
      case None => ""
    }
    V_%.text = testResult match {
      case Some(result) => percentFormat.format(result.findAnswers(answer => answer.vulgarAnswers.toList).size.toDouble / result.totalAnswerCount.max(1).toDouble)
      case None => ""
    }
    O_%.text = testResult match {
      case Some(result) => percentFormat.format(result.findAnswers(answer => answer.answerOriginalAnswers.toList).size.toDouble / result.totalAnswerCount.max(1).toDouble)
      case None => ""
    }
    VComparation.text = testResult match {
      case Some(result) => (result.findAnswers(answer => answer.vulgarAnswers.filter(_.vulgarity == Vulgarity.V1).toList).size
        - (result.findAnswers(answer => answer.vulgarAnswers.filter(_.vulgarity == Vulgarity.V2).toList).size
        + result.findAnswers(answer => answer.vulgarAnswers.filter(_.vulgarity == Vulgarity.V3).toList).size)).signum match {
        case -1 => "<html>V<sub>1</sub> &lt; V<sub>2</sub>+V<sub>3</sub></html>"
        case 0 => "<html>V<sub>1</sub> = V<sub>2</sub>+V<sub>3</sub></html>"
        case 1 => "<html>V<sub>1</sub> &gt; V<sub>2</sub>+V<sub>3</sub></html>"
      }
      case None => ""
    }
  }
}
