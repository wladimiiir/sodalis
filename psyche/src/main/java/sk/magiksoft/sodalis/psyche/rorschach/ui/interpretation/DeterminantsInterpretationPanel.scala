package sk.magiksoft.sodalis.psyche.rorschach.ui.interpretation

/*
 * Copyright (c) 2011
 */

import sk.magiksoft.sodalis.psyche.rorschach.event.TestResultChanged
import sk.magiksoft.sodalis.psyche.rorschach.RorschachManager
import scala.swing.{GridBagPanel, Label}
import java.awt.Color
import java.text.DecimalFormat
import sk.magiksoft.sodalis.psyche.rorschach.rule.{AffectiveTypeRules, SecondaryFormulaRules, ExperientalTypeRules}
import sk.magiksoft.sodalis.psyche.rorschach.entity.TestResult
import sk.magiksoft.sodalis.psyche.ui.LabeledGridBagPanelMixin
import sk.magiksoft.sodalis.core.locale.LocaleManager

/**
 * @author wladimiiir
 * @since 2011/5/22
 */

class DeterminantsInterpretationPanel extends GridBagPanel with InterpretationPanel with LabeledGridBagPanelMixin {
  private val totalFCount = createLabel
  private val totalQualitySignCount = createLabel
  private val F1Percent = createLabel
  private val F2Percent = createLabel
  private val experientalType = createLabel
  private val secondaryFormula = createLabel
  private val affectiveType = createLabel
  private val percentFormat = new DecimalFormat("0.00 %")
  private val ratioFormat = new DecimalFormat("0.#")

  addLabeledComponent(LocaleManager.getString("totalFCount"), totalFCount)
  addLabeledComponent(LocaleManager.getString("totalQualitySignCount"), totalQualitySignCount)
  addLabeledComponent(" ", new Label)
  addLabeledComponent(LocaleManager.getString("F1Percent"), F1Percent)
  addLabeledComponent(LocaleManager.getString("F2Percent"), F2Percent)
  addLabeledComponent(LocaleManager.getString("experientalType"), experientalType)
  addLabeledComponent(LocaleManager.getString("secondaryFormula"), secondaryFormula)
  addLabeledComponent(LocaleManager.getString("affectiveType"), affectiveType)

  protected def setupValues(testResult: Option[TestResult]) {
    testResult match {
      case Some(result) => {
        totalFCount.text = result.blotSignings.foldLeft(0) {
          (count, signing) => signing.answers.foldLeft(count) {
            (count, answer) => count + answer.answerDeterminants.count(_.determinant.name == "F")
          }
        }.toString
        totalQualitySignCount.text = result.blotSignings.foldLeft(0) {
          (count, signing) => signing.answers.foldLeft(count) {
            (count, answer) => count + answer.answerDeterminants.count(_.determinant.qualitySign)
          }
        }.toString
        F1Percent.text = percentFormat.format(RorschachManager.calculateF1Percent(result.blotSignings.toList))
        F2Percent.text = percentFormat.format(RorschachManager.calculateF2Percent(result.blotSignings.toList))
        RorschachManager.calculateExperientalType(result.blotSignings.toList) match {
          case (left, right) => experientalType.text = ratioFormat.format(left) + ":" + ratioFormat.format(right) +
            (ExperientalTypeRules.find((left, right)) match {
              case Some(foundType) => " - " + foundType
              case None => ""
            })
        }
        RorschachManager.calculateSecondaryFormula(result.blotSignings.toList) match {
          case (left, right) => secondaryFormula.text = ratioFormat.format(left) + ":" + ratioFormat.format(right) +
            (SecondaryFormulaRules.find((left, right)) match {
              case Some(foundType) => " - " + foundType
              case None => ""
            })
        }
        RorschachManager.calculateAffectiveType(result.blotSignings.toList) match {
          case (left, right) => affectiveType.text = ratioFormat.format(left) + ":" + ratioFormat.format(right) +
            (AffectiveTypeRules.find(result.blotSignings.foldLeft(0) {
              (count, signing) => count + signing.answers.size
            }, (left, right)) match {
              case Some(foundType) => " - " + foundType
              case None => ""
            })
        }
      }
      case None =>
    }
  }
}
