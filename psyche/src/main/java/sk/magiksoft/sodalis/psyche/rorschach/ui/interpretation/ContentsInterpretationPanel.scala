package sk.magiksoft.sodalis.psyche.rorschach.ui.interpretation

import sk.magiksoft.sodalis.psyche.rorschach.entity.TestResult
import sk.magiksoft.sodalis.psyche.rorschach.event.TestResultChanged
import java.text.DecimalFormat
import scala.swing.{GridBagPanel, Label}
import java.awt.Font
import sk.magiksoft.sodalis.psyche.ui.LabeledGridBagPanelMixin
import sk.magiksoft.sodalis.core.locale.LocaleManager

/*
* Copyright (c) 2011
*/

/**
 * @author wladimiiir
 * @since 2011/5/22
 */

class ContentsInterpretationPanel extends GridBagPanel with InterpretationPanel with LabeledGridBagPanelMixin {
  private val MCount = createLabel
  private val MdCount = createLabel
  private val M_% = createLabel
  private val T_% = createLabel
  private val Obj_% = createLabel
  private val percentFormat = new DecimalFormat("0.00 %")

  addLabeledComponent(LocaleManager.getString("MCount"), MCount)
  addLabeledComponent(LocaleManager.getString("MdCount"), MdCount)
  addLabeledComponent(LocaleManager.getString("M%"), M_%)
  addLabeledComponent(LocaleManager.getString("T%"), T_%)
  addLabeledComponent(LocaleManager.getString("Obj%"), Obj_%)

  def setupValues(testResult: Option[TestResult]) {
    MCount.text = testResult match {
      case Some(result) => result.findAnswers(answer => answer.contents.filter(_.name == "M").toList).size.toString
      case None => ""
    }
    MdCount.text = testResult match {
      case Some(result) => result.findAnswers(answer => answer.contents.filter(_.name == "Md").toList).size.toString
      case None => ""
    }
    M_%.text = testResult match {
      case Some(result) => percentFormat.format(result.findAnswers(answer => answer.contents.
        filter(c => c.name == "M" || c.name == "(M)" || c.name == "Md").toList).size.toDouble /
        result.findAnswers(answer => answer.contents.toList).size.max(1).toDouble)
      case None => ""
    }
    T_%.text = testResult match {
      case Some(result) => percentFormat.format(result.findAnswers(answer => answer.contents.
        filter(c => c.name == "T" || c.name == "Td").toList).size.toDouble /
        result.findAnswers(answer => answer.contents.toList).size.max(1).toDouble)
      case None => ""
    }
    Obj_%.text = testResult match {
      case Some(result) => percentFormat.format(result.findAnswers(answer => answer.contents.
        filter(c => c.name == "Obj").toList).size.toDouble /
        result.findAnswers(answer => answer.contents.toList).size.max(1).toDouble)
      case None => ""
    }
  }
}
