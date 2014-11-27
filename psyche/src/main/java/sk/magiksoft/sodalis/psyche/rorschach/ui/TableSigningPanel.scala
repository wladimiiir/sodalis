/*
 * Copyright (c) 2011
 */

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
import sk.magiksoft.sodalis.psyche.rorschach.event.TableAnswerEdited
import sk.magiksoft.sodalis.psyche.rorschach.event.TableAnswerAdded
import sk.magiksoft.sodalis.psyche.rorschach.event.RorschachTableChanged
import sk.magiksoft.sodalis.psyche.rorschach.event.TestResultChanged
import sk.magiksoft.sodalis.psyche.rorschach.event.TableSigningChanged
import sk.magiksoft.sodalis.psyche.rorschach.event.SigningMethodChanged
import sk.magiksoft.sodalis.psyche.rorschach.event.TableAnswerChanged
import sk.magiksoft.sodalis.core.factory.IconFactory
import scala.swing.BorderPanel.Position
import sk.magiksoft.sodalis.core.locale.LocaleManager
import scala.swing.GridBagPanel.{Anchor, Fill}
import scala.swing.TabbedPane.Page
import Swing._

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/12/11
 * Time: 7:47 PM
 * To change this template use File | Settings | File Templates.
 */

class TableSigningPanel extends GridBagPanel {
  private var currentTestResult: TestResult = _
  private var currentTable: RorschachTable = _
  private var currentTableAnswer: Option[TableAnswer] = None
  private var currentTableSigning: TableSigning = _

  initComponents()

  def testResult = currentTestResult

  private def initComponents() {
    val rorschachTables = PsycheDataManager.getRorschachTables.sortBy(_.index)
    val publishers = new ListBuffer[Publisher]
    type ReactionTime = FormattedTextField
    type TableTime = FormattedTextField

    def publishEvent(event: Event) {
      publishers.foreach(_.publish(event))
    }

    publishers += this
    reactions += {
      case TestResultChanged(result) => {
        currentTestResult = result
        val event = new TestResultChanged(result)
        publishers.filter(publisher => publisher ne this).foreach(_.publish(event))
        result.tableSignings.find(_.rorschachTable.index == 1) match {
          case Some(signing) => publishEvent(new RorschachTableChanged(signing.rorschachTable))
          case None => publishEvent(new RorschachTableChanged(rorschachTables(0)))
        }
      }
      case RorschachTableChanged(table) => {
        currentTable = table
        currentTestResult.tableSignings.find(signing => signing.rorschachTable eq table) match {
          case Some(signing) => publishEvent(new TableSigningChanged(signing))
          case None => {
            val signing = new TableSigning
            signing.rorschachTable = table
            currentTestResult.tableSignings += signing
            publishEvent(new TableSigningChanged(signing))
          }
        }
      }
      case TableAnswerChanged(answer) => {
        currentTableAnswer = answer
      }
      case TableSigningChanged(signing) => {
        currentTableSigning = signing
      }
      case TableAnswerEdited(answer) => {
        val event = new TableAnswerEdited(answer)
        publishers.filter(publisher => publisher ne this).foreach(_.publish(event))
      }
    }

    val tablePanel = new BorderPanel {
      val imagePanel = new ImagePanel() {
        setPreferredSize((250, 180))
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1, 5, 0, 2), BorderFactory.createLineBorder(Color.GRAY)))
      }
      val tableLabel = new Label
      val previousButton = new Button(Action("") {
        publishEvent(new RorschachTableChanged(rorschachTables(currentTable.index - 2)))
      }) {
        icon = IconFactory.getInstance().getIcon("previous")
        focusPainted = false
      }
      val nextButton = new Button(Action("") {
        publishEvent(new RorschachTableChanged(rorschachTables(currentTable.index)))
      }) {
        icon = IconFactory.getInstance().getIcon("next")
        focusPainted = false
      }
      add(new BorderPanel {
        border = BorderFactory.createEmptyBorder(5, 5, 0, 2)

        add(previousButton, Position.West)
        add(tableLabel, Position.Center)
        add(nextButton, Position.East)
      }, Position.North)
      add(Component.wrap(imagePanel), Position.Center)

      publishers += this
      reactions += {
        case RorschachTableChanged(table) => {
          if (!Hibernate.isInitialized(table.image)) {
            table.image = DefaultDataManager.getInstance().initialize(table.image)
          }

          imagePanel.setImage(table.image.getImage.asInstanceOf[BufferedImage])
          tableLabel.text = MessageFormat.format(LocaleManager.getString("tableNo"), table.index.toString())
          previousButton.enabled = table.index != 1
          nextButton.enabled = table.index != rorschachTables.size
        }
      }
    }
    val generalTablePanel = new GridBagPanel {
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
            currentTableSigning.reactionTime = text.toInt
          }
          case TableSigningChanged(signing) => {
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
      add(new Label(LocaleManager.getString("tableTime") + ":"), c)
      c.gridx += 1
      c.weightx = 1
      c.fill = Fill.Horizontal
      c.insets = new Insets(2, 2, 2, 0)
      add(new TableTime(format) {
        var adjusting = false
        publishers += this
        reactions += {
          case ValueChanged(_) if !adjusting && !text.isEmpty && text.forall(_.isDigit) => {
            currentTableSigning.tableTime = text.toInt
          }
          case TableSigningChanged(signing) => {
            adjusting = true
            text = format.format(signing.tableTime)
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

      val answerView = new ListView[TableAnswer]() {
        preferredSize = (250, 100)
        renderer = Renderer[TableAnswer, String](answer => (listData.indexOf(answer) + 1) + ". " + (if (answer.answer.isEmpty) " " else answer.answer))
        selection.intervalMode = IntervalMode.Single
        selection.reactions += {
          case ListSelectionChanged(_, _, adjusting) if !adjusting => publishEvent(new TableAnswerChanged(selection.items.headOption))
        }
        publishers += this
        reactions += {
          case TableAnswerEdited(_) => repaint()
          case TableSigningChanged(signing) => {
            listData = new ListBuffer[TableAnswer] ++ signing.answers
          }
        }
      }
      add(generalTablePanel, Position.North)
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
            val answer = new TableAnswer
            currentTableSigning.answers += answer
            publishEvent(new TableAnswerAdded(answer))
            answerView.listData :+= answer
            answerView.selection.indices += answerView.listData.size - 1
          }),
          new Button(Action(LocaleManager.getString("remove")) {

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
            currentTableAnswer match {
              case Some(answer) => {
                answer.answer = text
                publishEvent(new TableAnswerEdited(answer))
              }
              case None =>
            }
          }
          case TableAnswerChanged(answer) => answer match {
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
          case SigningMethod.General => new GeneralSigningPanel(TableSigningPanel.this)
          case SigningMethod.Aperception => new AperceptionSigningPanel(TableSigningPanel.this)
          case SigningMethod.Determinants => new DeterminantsSigningPanel(TableSigningPanel.this)
          case SigningMethod.Contents => new ContentsSigningPanel(TableSigningPanel.this)
          case SigningMethod.AnswerFrequency => new AnswerFrequencySigningPanel(TableSigningPanel.this)
          case SigningMethod.SpecialSigns => new SpecialSignsSigningPanel(TableSigningPanel.this)
        }
        publishers += panel
        panel
      }
    }
    val resultPanel = new TableInterpretationPanel
    publishers += resultPanel

    publishEvent(new TestResultChanged(new TestResult))
    publishEvent(new TableAnswerChanged(None))

    add(new BorderPanel {
      add(new BorderPanel {
        add(tablePanel, Position.North)
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