package sk.magiksoft.sodalis.psyche.rorschach.ui.signing

/*
 * Copyright (c) 2011
 */

import sk.magiksoft.sodalis.psyche.data.PsycheDataManager
import scala.swing.GridBagPanel.{Anchor, Fill}
import sk.magiksoft.sodalis.psyche.rorschach.entity._
import java.awt.{Insets, Font}
import javax.swing.BorderFactory
import sk.magiksoft.sodalis.psyche.rorschach.event.{BlotAnswerEdited, BlotAnswerChanged, RorschachBlotChanged}
import scala.swing.event.ButtonClicked
import scala.swing._
import sk.magiksoft.sodalis.core.locale.LocaleManager
import javax.swing.SpringLayout.Constraints
import scala.collection.mutable.ListBuffer
import sk.magiksoft.sodalis.psyche.rorschach.event.RorschachBlotChanged
import scala.Tuple3
import scala.swing.event.ButtonClicked
import sk.magiksoft.sodalis.psyche.rorschach.event.BlotAnswerChanged
import scala.Some
import sk.magiksoft.sodalis.psyche.rorschach.event.BlotAnswerEdited
import scala.Tuple2

/**
 * @author wladimiiir
 * @since 2011/5/13
 */

class AnswerFrequencySigningPanel(publisher: Publisher) extends GridBagPanel {
  private val vulgarAnswers = PsycheDataManager.getVulgarAnswers.sortBy(_.vulgarity).groupBy(_.blotIndex)
  private val originalAnswers = PsycheDataManager.getOriginalAnswers.groupBy(_.blotIndex)
  private var blotAnswer: Option[BlotAnswer] = None
  private val originalAnswerComponents = new ListBuffer[(OriginalAnswer, CheckBox, Option[QualitySign.Value])]
  private val vulgarAnswerComponents = new ListBuffer[(VulgarAnswer, CheckBox)]

  reactions += {
    case RorschachBlotChanged(blot) => {
      initComponents(originalAnswers(blot.index), vulgarAnswers(blot.index))
    }
    case BlotAnswerChanged(answer) => {
      blotAnswer = answer
      answer match {
        case Some(answer) => {
          for ((originalAnswer, checkBox, sign) <- originalAnswerComponents) {
            checkBox.enabled = true
            checkBox.selected = answer.answerOriginalAnswers
              .exists(aoa => aoa.originalAnswer == originalAnswer && aoa.qualitySign == sign)
          }
          for ((vulgarAnswer, checkBox) <- vulgarAnswerComponents) {
            checkBox.enabled = true
            checkBox.selected = answer.vulgarAnswers.contains(vulgarAnswer)
          }
        }
        case None => {
          for ((_, checkBox, _) <- originalAnswerComponents) {
            checkBox.enabled = false
            checkBox.selected = false
          }
          for ((_, checkBox) <- vulgarAnswerComponents) {
            checkBox.selected = false
            checkBox.enabled = false
          }
        }
      }
    }
  }


  private def initComponents(originalAnswers: List[OriginalAnswer], vulgarAnswers: List[VulgarAnswer]) {
    val buttons = new ListBuffer[CheckBox]

    originalAnswerComponents.clear()
    vulgarAnswerComponents.clear()
    val originalAnswerPanel = new GridBagPanel {
      val c = new Constraints
      c.grid = (0, 0)
      c.anchor = Anchor.West
      c.gridwidth = 3
      add(new Label(LocaleManager.getString("original")) {
        font = font.deriveFont(Font.BOLD, 14f)
      }, c)
      c.gridwidth = 1
      c.insets = new Insets(0, 5, 0, 0)
      for (originalAnswer <- originalAnswers) {
        c.gridy += 1
        c.gridx = 0
        add(createComponent(Option(QualitySign.+)), c)
        c.gridx += 1
        add(createComponent(Option(QualitySign.-)), c)
        c.gridx += 1
        add(createComponent(Option(QualitySign.+-)), c)
        def createComponent(sign: Option[QualitySign.Value]) =
          new CheckBox("<html><b>" + originalAnswer.name + "<sup>" + sign.get + "</sup></b></html>") {
            focusPainted = false
            reactions += {
              case ButtonClicked(b) => {
                if (b.selected) {
                  buttons.filter(_ ne b).foreach(_.selected = false)
                }
                blotAnswer match {
                  case Some(answer) if selected && !answer.answerOriginalAnswers
                    .exists(aoa => aoa.originalAnswer == originalAnswer && aoa.qualitySign == sign) => {
                    val answerOriginalAnswer = new AnswerOriginalAnswer
                    answerOriginalAnswer.originalAnswer = originalAnswer
                    answerOriginalAnswer.qualitySign = sign
                    answer.vulgarAnswers.clear()
                    answer.answerOriginalAnswers.clear()
                    answer.answerOriginalAnswers += answerOriginalAnswer
                    publisher.publish(new BlotAnswerEdited(answer))
                  }
                  case Some(answer) if !selected => answer.answerOriginalAnswers
                    .find(aoa => aoa.originalAnswer == originalAnswer && aoa.qualitySign == sign) match {
                    case Some(answerOriginalAnswer) => {
                      answer.answerOriginalAnswers -= answerOriginalAnswer
                      publisher.publish(new BlotAnswerEdited(answer))
                    }
                    case None =>
                  }
                  case _ =>
                }
              }
            }
            buttons += this
            originalAnswerComponents += Tuple3(originalAnswer, this, sign)
          }
      }
    }
    val vulgarAnswerPanel = new GridBagPanel {
      val c = new Constraints
      c.grid = (0, 0)
      c.anchor = Anchor.West
      c.gridwidth = 2
      add(new Label(LocaleManager.getString("vulgar")) {
        font = font.deriveFont(Font.BOLD, 14f)
      }, c)
      c.gridwidth = 1
      for (vulgarAnswer <- vulgarAnswers) {
        c.gridx = 0
        c.gridy += 1
        c.insets = new Insets(2, 5, 0, 0)
        add(new CheckBox("<html><b>" + vulgarAnswer.name + "</b></html>") {
          focusPainted = false
          reactions += {
            case ButtonClicked(b) => {
              if (selected) {
                buttons.filter(_ ne b).foreach(_.selected = false)
              }
              blotAnswer match {
                case Some(answer) if selected && !answer.vulgarAnswers.contains(vulgarAnswer) => {
                  answer.answerOriginalAnswers.clear()
                  answer.vulgarAnswers.clear()
                  answer.vulgarAnswers += vulgarAnswer
                  publisher.publish(new BlotAnswerEdited(answer))
                }
                case Some(answer) if !selected && answer.vulgarAnswers.contains(vulgarAnswer) => {
                  answer.vulgarAnswers -= vulgarAnswer
                  publisher.publish(new BlotAnswerEdited(answer))
                }
                case _ =>
              }
            }
          }
          buttons += this
          vulgarAnswerComponents += Tuple2(vulgarAnswer, this)
        }, c)
        c.gridx += 1
        c.insets = new Insets(2, 20, 0, 0)
        add(new Label(vulgarAnswer.vulgarity.toString) {
          font = font.deriveFont(Font.BOLD)
        }, c)
        c.gridx += 1
        c.insets = new Insets(2, 30, 0, 0)
        add(new Label(vulgarAnswer.localization), c)
      }
    }
    _contents.clear()
    add(new ScrollPane(new GridBagPanel {
      border = BorderFactory.createEmptyBorder(20, 20, 10, 10)

      add(originalAnswerPanel, new Constraints {
        grid = (0, 0)
        anchor = Anchor.West
      })
      add(vulgarAnswerPanel, new Constraints {
        grid = (0, 1)
        insets = new Insets(10, 0, 0, 0)
        anchor = Anchor.West
      })
      add(new BorderPanel, new Constraints {
        grid = (0, 2)
        weightx = 1
        weighty = 1
        fill = Fill.Both
      })
    }) {
      verticalScrollBar.unitIncrement = 20
    }, new Constraints {
      grid = (0, 0)
      weightx = 1
      weighty = 1
      fill = Fill.Both
    })
    revalidate()
    repaint()
  }
}
