package sk.magiksoft.sodalis.psyche.rorschach.ui.signing

import sk.magiksoft.sodalis.core.locale.LocaleManager
import scala.swing.GridBagPanel.{Fill, Anchor}
import java.awt.{Insets, GridBagConstraints}
import swing._
import event.ValueChanged
import javax.swing.BorderFactory
import swing.BorderPanel.Position
import sk.magiksoft.sodalis.psyche.rorschach.entity.BlotAnswer
import sk.magiksoft.sodalis.psyche.rorschach.event.{BlotAnswerChanged, BlotAnswerEdited}
import java.text.NumberFormat
import swing.Swing._
import collection.mutable.ListBuffer

/**
 * @author wladimiiir
 * @since 2011/5/13
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
    var blotAnswer: Option[BlotAnswer] = None

    reactions += {
      case BlotAnswerChanged(answer) => {
        blotAnswer = answer
        publishers.foreach(_.publish(new BlotAnswerChanged(answer)))
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
            blotAnswer match {
              case Some(answer) => {
                answer.myInterpretation = text
                publisher.publish(new BlotAnswerEdited(answer))
              }
              case None =>
            }
          }
          case BlotAnswerChanged(answer) => {
            blotAnswer match {
              case Some(answer) => {
                blotAnswer = None
                enabled = true
                text = answer.myInterpretation
              }
              case None => {
                enabled = false
                text = ""
              }
            }
            blotAnswer = answer
          }
        }
      }), Position.Center)
    }, c)

  }
}
