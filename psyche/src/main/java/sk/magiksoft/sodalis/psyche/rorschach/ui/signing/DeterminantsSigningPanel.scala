package sk.magiksoft.sodalis.psyche.rorschach.ui.signing

/*
 * Copyright (c) 2011
 */

import sk.magiksoft.sodalis.psyche.data.PsycheDataManager
import java.awt.{Insets}
import java.awt.event.ItemListener
import javax.swing.BorderFactory
import sk.magiksoft.sodalis.psyche.rorschach.entity.{AnswerDeterminant, BlotAnswer, QualitySign, Determinant}
import sk.magiksoft.sodalis.psyche.rorschach.event.{BlotAnswerEdited, BlotAnswerChanged}
import scala.swing.GridBagPanel.{Anchor, Fill}
import javax.swing.SpringLayout.Constraints
import scala.swing._
import scala.swing.event.ButtonClicked
import scala.collection.mutable.ListBuffer
import scala.Tuple3
import scala.swing.event.ButtonClicked
import sk.magiksoft.sodalis.psyche.rorschach.event.BlotAnswerChanged
import scala.Some
import sk.magiksoft.sodalis.psyche.rorschach.event.BlotAnswerEdited

/**
 * @author wladimiiir
 * @since 2011/5/13
 */

class DeterminantsSigningPanel(publisher: Publisher) extends GridBagPanel {

  initComponents()

  private def initComponents() {
    val determinants = PsycheDataManager.getDeterminants
    val determinantComponents = new ListBuffer[(Determinant, CheckBox, Option[QualitySign.Value])]
    var blotAnswer: Option[BlotAnswer] = None

    reactions += {
      case BlotAnswerChanged(answer) => {
        blotAnswer = answer
        answer match {
          case Some(answer) => {
            for ((_, checkBox, _) <- determinantComponents) {
              checkBox.selected = false
              checkBox.enabled = true
            }
            determinantComponents.filter(tuple => answer.answerDeterminants.exists(da => (da.determinant == tuple._1) && da.qualitySign == tuple._3))
              .foreach(_._2.selected = true)
          }
          case None => {
            for ((_, checkBox, _) <- determinantComponents) {
              checkBox.selected = false
              checkBox.enabled = false
            }
          }
        }
      }
    }

    val checkBoxPanel = new GridBagPanel {
      border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

      val c = new Constraints
      c.gridy = 0
      c.anchor = Anchor.West
      for (determinant <- determinants if determinant.qualitySign) {
        val buttons = new ListBuffer[CheckBox]
        c.gridx = 0
        c.insets = new Insets(2, 2, 0, 0)
        add(createCheckBox(Option(QualitySign.+)), c)
        c.gridx += 1
        c.insets = new Insets(2, 1, 0, 0)
        add(createCheckBox(Option(QualitySign.-)), c)
        c.gridx += 1
        add(createCheckBox(Option(QualitySign.+-)), c)
        c.gridx += 1
        c.insets = new Insets(2, 20, 0, 2)
        add(new Label(determinant.description), c)
        c.gridy += 1

        def createCheckBox(qualitySign: Option[QualitySign.Value]) =
          new CheckBox("<html><b>" + determinant.name + (qualitySign match {
            case Some(sign) => "<sup>" + sign.toString + "</sup>"
            case None => ""
          }) + "</b></html>") {

            focusPainted = false
            reactions += {
              case ButtonClicked(b) => {
                if (b.selected) {
                  buttons.filter(_ ne b).foreach(_.selected = false)
                }
                blotAnswer match {
                  case Some(answer) if selected && !answer.answerDeterminants.exists(ad => ad.qualitySign == qualitySign && ad.determinant == determinant) => {
                    val answerDeterminant = new AnswerDeterminant
                    answerDeterminant.determinant = determinant
                    answerDeterminant.qualitySign = qualitySign
                    answer.answerDeterminants --= answer.answerDeterminants.filter(_.determinant == determinant)
                    answer.answerDeterminants += answerDeterminant
                    publisher.publish(new BlotAnswerEdited(answer))
                  }
                  case Some(answer) if !selected => answer.answerDeterminants.find(ad => ad.qualitySign == qualitySign && ad.determinant == determinant) match {
                    case Some(answerDeterminant) => {
                      answer.answerDeterminants -= answerDeterminant
                      publisher.publish(new BlotAnswerEdited(answer))
                    }
                    case None =>
                  }
                  case _ =>
                }
              }
            }
            buttons += this
            determinantComponents += Tuple3(determinant, this, qualitySign)
          }
      }

      for (determinant <- determinants if !determinant.qualitySign) {
        val checkBox = new CheckBox("<html><b>" + determinant.name + "</b></html>") {
          focusPainted = false
          reactions += {
            case ButtonClicked(b) => {
              blotAnswer match {
                case Some(answer) if selected && !answer.answerDeterminants.exists(ad => ad.determinant == determinant) => {
                  val answerDeterminant = new AnswerDeterminant
                  answerDeterminant.determinant = determinant
                  answer.answerDeterminants += answerDeterminant
                  publisher.publish(new BlotAnswerEdited(answer))
                }
                case Some(answer) if !selected => answer.answerDeterminants.find(ad => ad.determinant == determinant) match {
                  case Some(answerDeterminant) => {
                    answer.answerDeterminants -= answerDeterminant
                    publisher.publish(new BlotAnswerEdited(answer))
                  }
                  case None =>
                }
                case _ =>
              }
            }
          }
          determinantComponents += Tuple3(determinant, this, None)
        }
        c.gridx = 0
        c.insets = new Insets(2, 2, 0, 0)
        c.gridwidth = 3
        add(checkBox, c)
        c.gridx = 3
        c.gridwidth = 1
        c.insets = new Insets(2, 20, 0, 2)
        add(new Label(determinant.description), c)
        c.gridy += 1
      }
    }

    add(new ScrollPane(checkBoxPanel) {
      verticalScrollBar.unitIncrement = 20
    }, new Constraints() {
      grid = (0, 0)
      fill = Fill.Both
      weightx = 1.0
      weighty = 1.0
    })
  }
}
