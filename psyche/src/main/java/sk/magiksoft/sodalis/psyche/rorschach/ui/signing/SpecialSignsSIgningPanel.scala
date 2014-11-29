package sk.magiksoft.sodalis.psyche.rorschach.ui.signing

/*
 * Copyright (c) 2011
 */

import sk.magiksoft.sodalis.psyche.data.PsycheDataManager
import java.awt.{GridBagConstraints, GridBagLayout}
import sk.magiksoft.sodalis.psyche.rorschach.entity.{BlotAnswer, SpecialSign}
import sk.magiksoft.sodalis.psyche.rorschach.event.BlotAnswerChanged
import scala.swing._
import scala.swing.GridBagPanel.Fill
import scala.collection.mutable.ListBuffer
import sk.magiksoft.sodalis.psyche.rorschach.event.BlotAnswerChanged
import scala.swing.event.ButtonClicked
import scala.Some
import sk.magiksoft.sodalis.psyche.rorschach.event.BlotAnswerEdited
import scala.Tuple2
import scala.swing.ScrollPane.BarPolicy
import org.jdesktop.swingx.JXTaskPane

/**
 * @author wladimiiir
 * @since 2011/5/13
 */

class SpecialSignsSigningPanel(publisher: Publisher) extends GridBagPanel {
  private type CategoryName = String
  private var blotAnswer: Option[BlotAnswer] = None
  private val specialSignComponents = new ListBuffer[(SpecialSign, CheckBox)]
  private val taskPanes = new ListBuffer[(CategoryName, JXTaskPane)]

  reactions += {
    case BlotAnswerChanged(answer) => {
      blotAnswer = answer
      answer match {
        case Some(answer) => {
          for ((specialSign, checkBox) <- specialSignComponents) {
            checkBox.enabled = true
            checkBox.selected = answer.specialSigns.contains(specialSign)
          }
        }
        case None => {
          for ((_, checkBox) <- specialSignComponents) {
            checkBox.enabled = false
            checkBox.selected = false
          }
        }
      }
      taskPanes.foreach(tuple => refreshTaskPaneTitle(tuple._2, tuple._1))
    }
  }

  initComponents()

  private def refreshTaskPaneTitle(taskPane: JXTaskPane, category: CategoryName) {
    val signsString = specialSignComponents.filter {
      tuple => tuple._1.category == category && tuple._2.selected
    }.map(_._1.name).sorted.mkString(", ")
    taskPane.setTitle(signsString match {
      case string: String if string.isEmpty => category
      case _ => category + ": " + signsString
    })
  }

  private def initComponents() {
    val specialSignsMap = PsycheDataManager.getSpecialSigns.sortWith((s1, s2) => s1.category.compare(s2.category) < 0).groupBy(_.category)
    val specialSignPanel = new GridBagPanel {
      val c = new Constraints {
        grid = (0, 0)
        weightx = 1
        fill = Fill.Both
      }

      for ((category, signs) <- specialSignsMap.toList.sortWith((t1, t2) => t1._1.compare(t2._1) < 0)) {
        val taskPane = new JXTaskPane() {
          val c = new GridBagConstraints()
          setCollapsed(true)
          setAnimated(false)
          setScrollOnExpand(true)
          setLayout(new GridBagLayout)
          setFocusable(false)
          taskPanes += Tuple2(category, this)
          for (specialSign <- signs.sortBy(_.name)) {
            c.gridx = 0
            c.gridy += 1
            c.weightx = 1
            c.insets = new Insets(2, 0, 0, 0)
            c.anchor = GridBagConstraints.WEST
            add(new CheckBox(specialSign.name) {
              focusPainted = false
              specialSignComponents += Tuple2(specialSign, this)
              reactions += {
                case ButtonClicked(_) => {
                  taskPanes.foreach(tuple => refreshTaskPaneTitle(tuple._2, tuple._1))
                  blotAnswer match {
                    case Some(answer) if selected && !answer.specialSigns.contains(specialSign) => {
                      answer.specialSigns += specialSign
                      publisher.publish(new BlotAnswerEdited(answer))
                    }
                    case Some(answer) if !selected && answer.specialSigns.contains(specialSign) => {
                      answer.specialSigns -= specialSign
                      publisher.publish(new BlotAnswerEdited(answer))
                    }
                    case _ =>
                  }
                }
              }
            }.peer, c)
            c.gridx += 1
            c.insets = new Insets(2, 20, 0, 0)
            add(new Label((specialSign.description)).peer, c)
          }
        }
        add(Component.wrap(taskPane), c)
        c.gridy += 1
      }
    }

    add(new ScrollPane(specialSignPanel) {
      verticalScrollBar.unitIncrement = 20
      verticalScrollBarPolicy = BarPolicy.Always
    }, new Constraints {
      grid = (0, 0)
      weightx = 1
      weighty = 1
      fill = Fill.Both
    })
  }
}
