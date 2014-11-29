package sk.magiksoft.sodalis.psyche.ui

import sk.magiksoft.sodalis.psyche.data.PsycheDataManager
import scala.swing.Swing._
import com.toedter.calendar.{JTextFieldDateEditor, JDateChooser}
import scala.swing.Component._
import sk.magiksoft.sodalis.psyche.PsychoTestModule
import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.person.PersonModule
import sk.magiksoft.sodalis.core.module.Module
import sk.magiksoft.sodalis.psyche.entity.{PsychoTestCreator, PsychoTest}
import sk.magiksoft.sodalis.psyche.rorschach.entity.BlotAnswer
import swing.ListView.{Renderer, IntervalMode}
import scala.swing.event.{ListSelectionChanged, ValueChanged}
import java.awt.Insets
import java.awt.Container._
import swing._
import sk.magiksoft.sodalis.core.action.Checker
import sk.magiksoft.sodalis.core.ui.{ISOptionPane, OkCancelDialog}
import java.util.Calendar
import scala.swing.GridBagPanel.{Fill, Anchor}
import javax.swing.SwingUtilities
import sk.magiksoft.sodalis.person.entity.PersonWrapper
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.person.ui.PersonChooserComponent

/**
 * @author wladimiiir
 * @since 2011/6/24
 */

class TestCreationDialog extends OkCancelDialog(LocaleManager.getString("psychoTest")) {
  initComponents()

  private def initComponents() {
    val dateChooser = new JDateChooser(new JTextFieldDateEditor("dd.MM.yyyy HH:mm", "##.##.#### ##:##", ' '))
    val personChooser = new PersonChooserComponent(classOf[PsychoTestModule], SodalisApplication.get.getModuleManager.getModuleBySuperClass(classOf[PersonModule]) match {
      case module: PersonModule => module.getClass.asInstanceOf[Class[PersonModule]]
      case _ => null
    })
    val psychoTests = new ListView[PsychoTestCreator](PsycheDataManager.getPsychoTestCreators) {
      selection.intervalMode = IntervalMode.Single
      renderer = Renderer[PsychoTestCreator, String](_.getPsychoTestName)
      selection.reactions += {
        case ListSelectionChanged(_, _, adjusting) if !adjusting => {
          getOkButton.setEnabled(!selection.indices.isEmpty)
        }
      }
    }

    val mainPanel = new GridBagPanel {
      val c = new Constraints()
      c.grid = (0, 0)
      c.anchor = Anchor.East
      c.insets = new Insets(10, 10, 0, 0)
      add(new Label(LocaleManager.getString("date")), c)

      c.gridx += 1
      c.anchor = Anchor.West
      c.insets = new Insets(10, 3, 0, 10)
      c.fill = Fill.Horizontal
      add(wrap(dateChooser), c)

      c.gridx = 0
      c.gridy += 1
      c.anchor = Anchor.East
      c.insets = new Insets(3, 10, 0, 0)
      c.fill = Fill.None
      add(new Label(LocaleManager.getString("testedPerson")), c)

      c.gridx += 1
      c.anchor = Anchor.West
      c.insets = new Insets(3, 3, 0, 10)
      c.fill = Fill.Horizontal
      add(personChooser, c)

      c.gridx = 0
      c.gridy += 1
      c.gridwidth = 2
      c.insets = new Insets(3, 10, 10, 10)
      add(new ScrollPane(psychoTests), c)
    }

    getOkButton.setEnabled(false)
    getOkButton.addActionListener(Swing.ActionListener(e => {
      personChooser.getPersonWrapper match {
        case wrapper: PersonWrapper if wrapper.getPerson != null => {
          val test = new PsychoTest
          test.date = dateChooser.getCalendar
          test.testedSubject = wrapper
          SwingUtilities.invokeLater(new Runnable {
            def run() {
              psychoTests.selection.items.head.createPsychoTest(test)
            }
          })
        }
        case _ =>
      }
    }))
    addOkActionChecker(new Checker {
      def check() = {
        personChooser.getPersonWrapper match {
          case wrapper: PersonWrapper if wrapper.getPerson != null => {
            true
          }
          case _ => {
            ISOptionPane.showMessageDialog(LocaleManager.getString("choosePerson"))
            false
          }
        }
      }
    })

    dateChooser.setCalendar(Calendar.getInstance())

    setMainPanel(mainPanel.peer)
    setSize(400, 300)
    setLocationRelativeTo(null)
  }
}
