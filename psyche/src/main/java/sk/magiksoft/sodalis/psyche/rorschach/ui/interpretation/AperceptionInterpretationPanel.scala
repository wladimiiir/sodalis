package sk.magiksoft.sodalis.psyche.rorschach.ui.interpretation

import sk.magiksoft.sodalis.psyche.rorschach.RorschachManager
import sk.magiksoft.sodalis.psyche.rorschach.entity.TestResult
import javax.swing.BorderFactory
import java.awt.{Color, Font}
import sk.magiksoft.sodalis.psyche.ui.LabeledGridBagPanelMixin
import scala.swing._
import sk.magiksoft.sodalis.core.locale.LocaleManager
import scala.Some
import scala.swing.BorderPanel.Position
import Swing._

/*
* Copyright (c) 2011
*/


/**
 * @author wladimiiir
 * @since 2011/5/22
 */

class AperceptionInterpretationPanel extends GridBagPanel with InterpretationPanel with LabeledGridBagPanelMixin {
  private val answerCount = createLabel
  private val G = createLabel
  private val D = createLabel
  private val Dd = createLabel
  private val zw = createLabel
  private val Do = createLabel
  private val DGkonf = createLabel
  private val DGkomb = createLabel
  private val aperceptionTypePanel = new BorderPanel {
    preferredSize = (100, 23)

    def addComponent(c: Component, l: BorderPanel#Constraints) {
      super.add(c, l)
    }
  }

  addLabeledComponent(LocaleManager.getString("totalAnswerCount"), answerCount)
  addLabeledComponent(LocaleManager.getString("GCount"), G)
  addLabeledComponent(LocaleManager.getString("DCount"), D)
  addLabeledComponent(LocaleManager.getString("DdCount"), Dd)
  addLabeledComponent(LocaleManager.getString("zwCount"), zw)
  addLabeledComponent(LocaleManager.getString("DoCount"), Do)
  addLabeledComponent(LocaleManager.getString("DGkonfCount"), DGkonf)
  addLabeledComponent(LocaleManager.getString("DGkombCount"), DGkomb)

  protected def setupValues(testResult: Option[TestResult]) {
    val countMap = testResult match {
      case Some(result) => {
        answerCount.text = result.blotSignings.foldLeft(0)((count, signing) => count + signing.answers.size).toString
        RorschachManager.calculateAperceptionEntryGroupCount(result.blotSignings.toList)
      }
      case None => {
        answerCount.text = "0"
        Map[String, Int]()
      }
    }

    G.text = countMap.get("G") match {
      case Some(count) => count.toString
      case None => "0"
    }
    D.text = countMap.get("D") match {
      case Some(count) => count.toString
      case None => "0"
    }
    Dd.text = countMap.get("Dd") match {
      case Some(count) => count.toString
      case None => "0"
    }
    zw.text = countMap.get("zw") match {
      case Some(count) => count.toString
      case None => "0"
    }
    Do.text = countMap.get("Do") match {
      case Some(count) => count.toString
      case None => "0"
    }
    DGkonf.text = countMap.get("DGkonf") match {
      case Some(count) => count.toString
      case None => "0"
    }
    DGkomb.text = countMap.get("DGkomb") match {
      case Some(count) => count.toString
      case None => "0"
    }

    val aperceptionType = RorschachManager.determineAperceptionType(answerCount.text.toInt, countMap)
    aperceptionTypePanel.addComponent(new FlowPanel(FlowPanel.Alignment.Left)(aperceptionType.map {
      val DoubleUnderlined = """(.+)__""".r
      val Underlined = """(.+)_""".r
      _ match {
        case DoubleUnderlined(entry) => new Label(entry) {
          font = font.deriveFont(Font.BOLD)
          verticalTextPosition = Alignment.Top
          minimumSize = (0, 23)
          border = BorderFactory.createCompoundBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK),
            BorderFactory.createEmptyBorder(0, 0, 1, 0)), BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK))
        }
        case Underlined(entry) => new Label(entry) {
          font = font.deriveFont(Font.BOLD)
          verticalTextPosition = Alignment.Top
          minimumSize = (0, 23)
          border = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK)
        }
        case entry: String => new Label(entry) {
          font = font.deriveFont(Font.BOLD)
          verticalTextPosition = Alignment.Top
          minimumSize = (0, 23)
        }
      }
    }.toArray: _*) {
      hGap = 1
    }, Position.Center)
  }
}
