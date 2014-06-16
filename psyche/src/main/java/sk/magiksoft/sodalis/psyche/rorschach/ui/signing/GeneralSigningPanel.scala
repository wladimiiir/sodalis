/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.ui.signing

import sk.magiksoft.sodalis.core.locale.LocaleManager
import swing.GridBagPanel.Anchor
import java.awt.{Insets, GridBagConstraints}
import swing._
import event.ValueChanged
import javax.swing.BorderFactory
import swing.BorderPanel.Position
import sk.magiksoft.sodalis.psyche.rorschach.entity.TableAnswer
import sk.magiksoft.sodalis.psyche.rorschach.event.{TableAnswerChanged, TableAnswerEdited}
import java.text.NumberFormat
import swing.Swing._
import collection.mutable.ListBuffer

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/13/11
 * Time: 11:01 AM
 * To change this template use File | Settings | File Templates.
 */

class GeneralSigningPanel(publisher: Publisher) extends GridBagPanel {
  initComponents()

  private def initComponents() {
    val c = new Constraints
    val format = NumberFormat.getInstance()
    val publishers = new ListBuffer[Publisher]
    type ReactionTime = FormattedTextField
    type AnswerTime = FormattedTextField
    type MyInterpretation = TextArea
    var tableAnswer: Option[TableAnswer] = None

    reactions += {
      case TableAnswerChanged(answer) => {
        tableAnswer = answer
        publishers.foreach(_.publish(new TableAnswerChanged(answer)))
      }
    }

    c.grid = (0, 0)
    c.fill = Fill.Horizontal
    c.weightx = 1.0
    c.gridwidth = 2
    c.insets = new Insets(10, 5, 5, 5)
    add(new BorderPanel {
      border = BorderFactory.createTitledBorder(LocaleManager.getString("myInterpretation"))
      add(new ScrollPane(new MyInterpretation(5, 5) {
        publishers += this
        reactions += {
          case ValueChanged(_) => {
            tableAnswer match {
              case Some(answer) => {
                answer.myInterpretation = text
                publisher.publish(new TableAnswerEdited(answer))
              }
              case None =>
            }
          }
          case TableAnswerChanged(answer) => {
            tableAnswer match {
              case Some(answer) => {
                tableAnswer = None
                enabled = true
                text = answer.myInterpretation
              }
              case None => {
                enabled = false
                text = ""
              }
            }
            tableAnswer = answer
          }
        }
      }), Position.Center)
    }, c)

  }
}