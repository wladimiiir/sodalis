package sk.magiksoft.sodalis.psyche.rorschach.ui

import javax.swing.BorderFactory
import java.awt.Color
import org.hibernate.Hibernate
import sk.magiksoft.sodalis.core.data.DefaultDataManager
import sk.magiksoft.sodalis.psyche.data.PsycheDataManager
import java.awt.image.BufferedImage
import scala.swing.ListView.{Renderer, IntervalMode}
import sk.magiksoft.sodalis.psyche.rorschach.event._
import java.text.{NumberFormat, MessageFormat}
import signing._
import sk.magiksoft.sodalis.psyche.rorschach.entity._
import scala.swing._
import scala.collection.mutable.ListBuffer
import scala.swing.event.{SelectionChanged, ListSelectionChanged, ValueChanged, Event}
import sk.magiksoft.sodalis.core.ui.ImagePanel
import scala.Some
import sk.magiksoft.sodalis.psyche.rorschach.event.BlotAnswerEdited
import sk.magiksoft.sodalis.psyche.rorschach.event.BlotAnswerAdded
import sk.magiksoft.sodalis.psyche.rorschach.event.RorschachBlotChanged
import sk.magiksoft.sodalis.psyche.rorschach.event.TestResultChanged
import sk.magiksoft.sodalis.psyche.rorschach.event.BlotSigningChanged
import sk.magiksoft.sodalis.psyche.rorschach.event.SigningMethodChanged
import sk.magiksoft.sodalis.psyche.rorschach.event.BlotAnswerChanged
import sk.magiksoft.sodalis.core.factory.IconFactory
import scala.swing.BorderPanel.Position
import sk.magiksoft.sodalis.core.locale.LocaleManager
import scala.swing.GridBagPanel.{Anchor, Fill}
import scala.swing.TabbedPane.Page
import Swing._

/**
 * @author wladimiiir
 * @since 2011/5/12
 *         To change this template use File | Settings | File Templates.
 */

class BlotSigningPanel extends GridBagPanel {
  private var currentTestResult: TestResult = _
  private var currentBlot: RorschachBlot = _
  private var currentBlotAnswer: Option[BlotAnswer] = None
  private var currentBlotSigning: BlotSigning = _

  initComponents()

  def testResult = currentTestResult

  private def initComponents() {
    val rorschachBlots = PsycheDataManager.getRorschachBlots.sortBy(_.index)
    val publishers = new ListBuffer[Publisher]
    type ReactionTime = FormattedTextField
    type BlotTime = FormattedTextField

    def publishEvent(event: Event) {
      publishers.foreach(_.publish(event))
    }

    publishers += this
    reactions += {
      case TestResultChanged(result) => {
        currentTestResult = result
        val event = new TestResultChanged(result)
        publishers.filter(publisher => publisher ne this).foreach(_.publish(event))
        result.blotSignings.find(_.rorschachBlot.index == 1) match {
          case Some(signing) => publishEvent(new RorschachBlotChanged(signing.rorschachBlot))
          case None => publishEvent(new RorschachBlotChanged(rorschachBlots(0)))
        }
      }
      case RorschachBlotChanged(blot) => {
        currentBlot = blot
        currentTestResult.blotSignings.find(signing => signing.rorschachBlot.id == blot.id) match {
          case Some(signing) => publishEvent(new BlotSigningChanged(signing))
          case None => {
            val signing = new BlotSigning
            signing.rorschachBlot = blot
            currentTestResult.blotSignings += signing
            publishEvent(new BlotSigningChanged(signing))
          }
        }
      }
      case BlotAnswerChanged(answer) => {
        currentBlotAnswer = answer
      }
      case BlotSigningChanged(signing) => {
        currentBlotSigning = signing
      }
      case BlotAnswerEdited(answer) => {
        val event = new BlotAnswerEdited(answer)
        publishers.filter(publisher => publisher ne this).foreach(_.publish(event))
      }
    }

    val blotPanel = new BorderPanel {
      val imagePanel = new ImagePanel() {
        setPreferredSize((250, 180))
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1, 5, 0, 2), BorderFactory.createLineBorder(Color.GRAY)))
      }
      val blotLabel = new Label
      val previousButton = new Button(Action("") {
        publishEvent(new RorschachBlotChanged(rorschachBlots(currentBlot.index - 2)))
      }) {
        icon = IconFactory.getInstance().getIcon("previous")
        focusPainted = false
      }
      val nextButton = new Button(Action("") {
        publishEvent(new RorschachBlotChanged(rorschachBlots(currentBlot.index)))
      }) {
        icon = IconFactory.getInstance().getIcon("next")
        focusPainted = false
      }
      add(new BorderPanel {
        border = BorderFactory.createEmptyBorder(5, 5, 0, 2)

        add(previousButton, Position.West)
        add(blotLabel, Position.Center)
        add(nextButton, Position.East)
      }, Position.North)
      add(Component.wrap(imagePanel), Position.Center)

      publishers += this
      reactions += {
        case RorschachBlotChanged(blot) => {
          if (!Hibernate.isInitialized(blot.image)) {
            blot.image = DefaultDataManager.getInstance().initialize(blot.image)
          }

          imagePanel.setImage(blot.image.getImage.asInstanceOf[BufferedImage])
          blotLabel.text = MessageFormat.format(LocaleManager.getString("blotNo"), blot.index.toString)
          previousButton.enabled = blot.index != 1
          nextButton.enabled = blot.index != rorschachBlots.size
        }
      }
    }
    val generalBlotPanel = new GridBagPanel {
      val format = NumberFormat.getInstance()
      val c = new Constraints
      c.grid = (0, 0)
      c.anchor = Anchor.East
      c.insets = new Insets(5, 5, 0, 0)
      add(new Label(LocaleManager.getString("reactionTime") + ":"), c)
      c.gridx += 1
      c.weightx = 1
      c.fill = Fill.Horizontal
      c.insets = new Insets(5, 2, 0, 0)
      add(new ReactionTime(format) {
        var adjusting = false
        publishers += this
        reactions += {
          case ValueChanged(_) if !adjusting && !text.isEmpty && text.forall(_.isDigit) => {
            currentBlotSigning.reactionTime = text.toInt
          }
          case BlotSigningChanged(signing) => {
            adjusting = true
            text = format.format(signing.reactionTime)
            adjusting = false
          }
        }
      }, c)
      c.gridx += 1
      c.insets = new Insets(5, 2, 0, 5)
      c.weightx = 0
      c.fill = Fill.None
      add(new Label("s"), c)
      c.gridx = 0
      c.gridy += 1
      c.insets = new Insets(2, 5, 2, 0)
      add(new Label(LocaleManager.getString("blotTime") + ":"), c)
      c.gridx += 1
      c.weightx = 1
      c.fill = Fill.Horizontal
      c.insets = new Insets(2, 2, 2, 0)
      add(new BlotTime(format) {
        var adjusting = false
        publishers += this
        reactions += {
          case ValueChanged(_) if !adjusting && !text.isEmpty && text.forall(_.isDigit) => {
            currentBlotSigning.blotTime = text.toInt
          }
          case BlotSigningChanged(signing) => {
            adjusting = true
            text = format.format(signing.blotTime)
            adjusting = false
          }
        }
      }, c)
      c.gridx += 1
      c.insets = new Insets(5, 2, 0, 5)
      c.weightx = 0
      c.fill = Fill.None
      add(new Label("s"), c)
    }
    val answersPanel = new BorderPanel {
      border = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 5, 0, 2), BorderFactory.createLineBorder(Color.GRAY))

      val answerView = new ListView[BlotAnswer]() {
        preferredSize = (250, 100)
        renderer = Renderer[BlotAnswer, String](answer => (listData.indexOf(answer) + 1) + ". " + (if (answer.answer.isEmpty) " " else answer.answer))
        selection.intervalMode = IntervalMode.Single
        selection.reactions += {
          case ListSelectionChanged(_, _, adjusting) if !adjusting => publishEvent(new BlotAnswerChanged(selection.leadIndex match {
            case index: Int if index >= 0 && index < listData.size => Some(listData(index))
            case _ => None
          }))
        }
        publishers += this
        reactions += {
          case BlotAnswerEdited(_) => repaint()
          case BlotSigningChanged(signing) => {
            listData = new ListBuffer[BlotAnswer] ++ signing.answers
            publishEvent(BlotAnswerChanged(None))
          }
        }
      }
      add(generalBlotPanel, Position.North)
      add(new BorderPanel {
        add(new Label(LocaleManager.getString("answers")) {
          border = BorderFactory.createEmptyBorder(3, 5, 2, 5)
          horizontalAlignment = Alignment.Left
        }, Position.North)
        add(new ScrollPane(answerView) {
          border = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5),
            BorderFactory.createLineBorder(Color.GRAY))
        }, Position.Center)
        add(new FlowPanel(FlowPanel.Alignment.Right)(
          new Button(Action(LocaleManager.getString("add")) {
            val answer = new BlotAnswer
            currentBlotSigning.answers += answer
            publishEvent(new BlotAnswerAdded(answer))
            answerView.listData :+= answer
            answerView.selection.indices += answerView.listData.size - 1
          }),
          new Button(Action(LocaleManager.getString("remove")) {
            answerView.selection.leadIndex match {
              case index: Int if index >= 0 && index < answerView.listData.size =>
                val answer = answerView.listData(index)
                currentBlotSigning.answers -= answer
                publishEvent(new BlotAnswerRemoved(answer))
                answerView.listData = answerView.listData diff List(answer)
                answerView.selection.indices -= index
              case _ =>
            }
            reactions += {
              case BlotAnswerChanged(answer) => enabled = answer.isDefined
            }
          })
        ), Position.South)
      }, Position.Center)
    }
    val answerDefinitionPanel = new BorderPanel {
      add(new Label(LocaleManager.getString("answer")) {
        horizontalAlignment = Alignment.Left
        border = BorderFactory.createEmptyBorder(13, 0, 1, 5)
      }, Position.North)
      add(new ScrollPane(new TextArea(2, 10) {
        enabled = false

        publishers += this
        reactions += {
          case ValueChanged(_) => {
            currentBlotAnswer match {
              case Some(answer) => {
                answer.answer = text
                publishEvent(new BlotAnswerEdited(answer))
              }
              case None =>
            }
          }
          case BlotAnswerChanged(answer) => answer match {
            case Some(answer) => {
              text = answer.answer
              requestFocus()
              enabled = true
            }
            case None => {
              text = ""
              enabled = false
            }
          }
        }
      }) {
        border = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5),
          BorderFactory.createLineBorder(Color.GRAY))
      }, Position.Center)
    }
    val tabbedPane = new TabbedPane {
      border = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5), border)
      focusable = false

      selection.reactions += {
        case SelectionChanged(pane: TabbedPane) =>
          publishEvent(new SigningMethodChanged(SigningMethod.valueList(pane.selection.index)))
      }
      for (method <- SigningMethod.valueList) {
        pages += new Page(method.id + ". " + method.toString, createMethodPanel(method))
      }

      def createMethodPanel(method: SigningMethod.Value): Component = {
        val panel = method match {
          case SigningMethod.General => new GeneralSigningPanel(BlotSigningPanel.this)
          case SigningMethod.Aperception => new AperceptionSigningPanel(BlotSigningPanel.this)
          case SigningMethod.Determinants => new DeterminantsSigningPanel(BlotSigningPanel.this)
          case SigningMethod.Contents => new ContentsSigningPanel(BlotSigningPanel.this)
          case SigningMethod.AnswerFrequency => new AnswerFrequencySigningPanel(BlotSigningPanel.this)
          case SigningMethod.SpecialSigns => new SpecialSignsSigningPanel(BlotSigningPanel.this)
        }
        publishers += panel
        panel
      }
    }
    val resultPanel = new BlotInterpretationPanel
    publishers += resultPanel

    publishEvent(new TestResultChanged(new TestResult))
    publishEvent(new BlotAnswerChanged(None))

    add(new BorderPanel {
      add(new BorderPanel {
        add(blotPanel, Position.North)
        add(answersPanel, Position.Center)
      }, Position.West)
      add(new BorderPanel {
        add(answerDefinitionPanel, Position.North)
        add(tabbedPane, Position.Center)
        add(resultPanel, Position.South)
      }, Position.Center)
    }, new Constraints {
      grid = (0, 0)
      weightx = 1
      weighty = 1
      fill = Fill.Both
    })
  }


}
