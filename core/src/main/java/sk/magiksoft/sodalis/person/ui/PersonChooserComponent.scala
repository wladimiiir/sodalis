package sk.magiksoft.sodalis.person.ui

import swing._
import event.ButtonClicked
import sk.magiksoft.sodalis.core.module.Module
import sk.magiksoft.sodalis.core.factory.IconFactory
import sk.magiksoft.sodalis.person.entity.{PersonWrapper, Person}
import sk.magiksoft.sodalis.core.context.Context
import scala.collection.JavaConversions._
import sk.magiksoft.swing.PopupTextField
import sk.magiksoft.sodalis.core.utils.DatabaseEntityUpdatedList
import sk.magiksoft.sodalis.core.action.ContextTransferAction
import javax.swing.{SwingUtilities, BorderFactory}
import java.awt.{Window, Color}
import sk.magiksoft.sodalis.core.SodalisApplication

/**
 * @author wladimiiir
 * @since 2010/12/3
 */

class PersonChooserComponent(fromModuleClass: Class[_ <: Module], personModuleClass: Class[_ <: Module]) extends BorderPanel {
  private var window: Option[Window] = None
  protected val personField = new PopupTextField[Person](new DatabaseEntityUpdatedList[Person](classOf[Person])) {
    setBorder(BorderFactory.createEmptyBorder)
    setEditable(true)
  }
  protected val choosePersonAction = new ContextTransferAction(fromModuleClass, personModuleClass) {
    override def finalize(context: Context) {
      window match {
        case Some(window) => SwingUtilities.invokeLater(new Runnable {
          def run() {
            window.setVisible(true)
          }
        })
        case None =>
      }
      context match {
        case context: Context => context.getSelectedEntities match {
          case list: java.util.List[_] => asScalaBuffer(list).headOption match {
            case Some(entity) => personField.setSelectedItem(entity.asInstanceOf[Person])
            case _ =>
          }
          case _ =>
        }
        case _ =>
      }
      SwingUtilities.invokeLater(new Runnable {
        def run() {
          personField.grabFocus()
        }
      })
    }

    override def initialize(context: Context) = true
  }
  protected val choosePersonButton = new Button {
    icon = IconFactory.getInstance.getIcon("pickUp")
    focusable = false
    border = BorderFactory.createMatteBorder(0, 1, 0, 0, Color.GRAY)
    reactions += {
      case ButtonClicked(_) => {
        SwingUtilities.getWindowAncestor(peer) match {
          case sa: Window if (sa == SodalisApplication.get.getMainFrame) =>
          case w: Window => {
            window = Option(w)
            w.setVisible(false)
          }
          case _ =>
        }
        choosePersonAction.actionPerformed(null)
      }
    }
  }

  val scalaPersonField = Component.wrap(personField)
  add(scalaPersonField, BorderPanel.Position.Center)
  add(choosePersonButton, BorderPanel.Position.East)
  border = BorderFactory.createLineBorder(Color.GRAY)
  listenTo(scalaPersonField)
  listenTo(choosePersonButton)

  override def requestFocus() {
    personField.requestFocus()
  }

  def getPersonWrapper = personField.getSelectedItem match {
    case person: Person => new PersonWrapper(person)
    case _ => new PersonWrapper(personField.getText)
  }

  def setPersonWrapper(personWrapper: PersonWrapper) {
    personWrapper.getPerson match {
      case person: Person => personField.setSelectedItem(person)
      case _ => personField.setText(personWrapper.getPersonName)
    }
  }
}
