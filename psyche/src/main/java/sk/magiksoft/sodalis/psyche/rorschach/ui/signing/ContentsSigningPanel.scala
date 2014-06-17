/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.ui.signing

import sk.magiksoft.sodalis.psyche.data.PsycheDataManager
import scala.Tuple2
import swing.GridBagPanel._
import java.awt.Insets
import swing._
import scala.swing.event.{ValueChanged, ButtonClicked}
import javax.swing.BorderFactory
import javax.management.remote.rmi._RMIConnection_Stub
import sk.magiksoft.sodalis.psyche.rorschach.entity.{TableAnswer, Content => RContent}
import sk.magiksoft.sodalis.psyche.rorschach.event.{TableAnswerEdited, TableAnswerChanged}
import scala.collection.mutable.ListBuffer

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/13/11
 * Time: 11:01 AM
 * To change this template use File | Settings | File Templates.
 */

class ContentsSigningPanel(publisher: Publisher) extends GridBagPanel {
  initComponents()

  private def initComponents() {
    val rorschachContents = PsycheDataManager.getContents
    val contentComponents = new ListBuffer[(RContent, CheckBox)]
    val contenstText = new TextField {
      reactions += {
        case ValueChanged(_) => {
          val contentStrings = text.split(",").map(_.trim())
          for ((content, checkBox) <- contentComponents) {
            checkBox.selected = contentStrings.contains(content.name)
          }
        }
      }
    }

    var tableAnswer: Option[TableAnswer] = None
    reactions += {
      case TableAnswerChanged(answer) => {
        tableAnswer = answer
        tableAnswer match {
          case Some(answer) => {
            contentComponents.foreach(_._2.enabled = true)
            contenstText.text = answer.contents.map(_.name).mkString(", ")
          }
          case None => {
            contentComponents.foreach(_._2.enabled = false)
            contenstText.text = ""
          }
        }
      }
    }

    val checkBoxPanel = new GridBagPanel {
      border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

      val c = new Constraints

      c.gridy = 0
      c.anchor = Anchor.West
      for (content <- rorschachContents) {
        val checkBox = new CheckBox("<html><b>" + content.name + "</b></html>") {
          focusPainted = false
          contentComponents += Tuple2(content, this)
          reactions += {
            case ButtonClicked(_) => {
              contenstText.text = contentComponents.filter(_._2.selected).map(_._1.name).mkString(", ")
              tableAnswer match {
                case Some(answer) if selected && !answer.contents.contains(content) => {
                  answer.contents += content
                  publisher.publish(new TableAnswerEdited(answer))
                }
                case Some(answer) if !selected && answer.contents.contains(content) => {
                  answer.contents -= content
                  publisher.publish(new TableAnswerEdited(answer))
                }
                case _ =>
              }
            }
          }
        }
        c.gridx = 0
        c.insets = new Insets(2, 2, 0, 0)
        add(checkBox, c)
        c.gridx += 1
        c.insets = new Insets(2, 20, 0, 2)
        add(new Label(content.description), c)
        c.gridy += 1
      }
    }

    add(contenstText, new Constraints {
      grid = (0, 0)
      fill = Fill.Horizontal
      weightx = 1.0
    })
    add(new ScrollPane(checkBoxPanel) {
      verticalScrollBar.unitIncrement = 20
    }, new Constraints {
      grid = (0, 1)
      fill = Fill.Both
      weightx = 1.0
      weighty = 1.0
    })
  }
}