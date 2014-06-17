/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.ui.signing

import sk.magiksoft.sodalis.psyche.data.PsycheDataManager
import scala.swing.GridBagPanel.{Anchor, Fill}
import java.awt.Insets
import collection.mutable.ListBuffer
import sk.magiksoft.sodalis.psyche.rorschach.entity.{TableAnswer, Aperception}
import sk.magiksoft.sodalis.psyche.rorschach.event.{TableAnswerEdited, TableAnswerChanged}
import com.sun.java.swing.SwingUtilities3
import sun.swing.SwingUtilities2
import javax.swing.SpringLayout.Constraints
import scala.swing._
import sk.magiksoft.sodalis.psyche.rorschach.event.TableAnswerChanged
import scala.Some
import sk.magiksoft.sodalis.psyche.rorschach.event.TableAnswerEdited
import scala.Tuple2

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/12/11
 * Time: 8:06 PM
 * To change this template use File | Settings | File Templates.
 */

class AperceptionSigningPanel(publisher: Publisher) extends GridBagPanel {
  initComponents()

  private def initComponents() {
    val aperceptions = PsycheDataManager.getAperceptions
    val aperceptionComponents = new ListBuffer[(Aperception, CheckBox)]
    var tableAnswer: Option[TableAnswer] = None

    reactions += {
      case TableAnswerChanged(answer) => {
        tableAnswer = answer
        answer match {
          case Some(answer) => {
            for ((aperception, checkBox) <- aperceptionComponents) {
              checkBox.selected = false
              checkBox.enabled = true
            }
            aperceptionComponents.filter(tuple => answer.aperceptions.contains(tuple._1)).foreach(_._2.selected = true)
          }
          case None => {
            for ((_, checkBox) <- aperceptionComponents) {
              checkBox.selected = false
              checkBox.enabled = false
            }
          }
        }
      }
    }
    val checkBoxPanel = new GridBagPanel {
      val c = new Constraints

      c.gridy = 0
      c.anchor = Anchor.West
      for (aperception <- aperceptions) {
        c.gridx = 0
        c.insets = new Insets(2, 2, 0, 0)
        add(new CheckBox("<html><b>" + aperception.name + "</b></hmtl>") {
          focusPainted = false
          reactions += {
            case ButtonClicked(_) => {
              tableAnswer match {
                case Some(answer) if selected && !answer.aperceptions.exists(a => a eq aperception) => {
                  answer.aperceptions.clear()
                  aperception.name match {
                    case name: String if name == "G" => {
                      aperceptionComponents.find(tuple => tuple._1.name == "zw" && tuple._2.selected) match {
                        case Some((aperception, _)) => answer.aperceptions += aperception
                        case None =>
                      }
                      aperceptionComponents.filter(tuple => (tuple._2 ne this) && (tuple._1.name != "zw")).foreach(_._2.selected = false)
                    }
                    case name: String if name == "zw" => {
                      aperceptionComponents.find(tuple => tuple._1.name == "G" && tuple._2.selected) match {
                        case Some((aperception, _)) => answer.aperceptions += aperception
                        case None =>
                      }
                      aperceptionComponents.filter(tuple => (tuple._2 ne this) && (tuple._1.name != "G")).foreach(_._2.selected = false)
                    }
                    case _ => {
                      aperceptionComponents.filter(_._2 ne this).foreach(_._2.selected = false)
                    }
                  }
                  answer.aperceptions += aperception
                  publisher.publish(new TableAnswerEdited(answer))
                }
                case Some(answer) if !selected && answer.aperceptions.exists(a => a eq aperception) => {
                  answer.aperceptions -= aperception
                  publisher.publish(new TableAnswerEdited(answer))
                }
                case _ =>
              }
            }
          }
          aperceptionComponents += Tuple2(aperception, this)
        }, c)
        c.gridx = 1
        c.insets = new Insets(2, 20, 0, 2)
        add(new Label(aperception.description), c)
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