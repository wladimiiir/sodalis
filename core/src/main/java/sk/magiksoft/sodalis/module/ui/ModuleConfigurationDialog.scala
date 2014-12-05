package sk.magiksoft.sodalis.module.ui

import java.awt.event.ActionEvent
import java.awt.{Window, BorderLayout}
import javax.swing.AbstractAction

import sk.magiksoft.sodalis.core.factory.IconFactory
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.module.ModuleManager
import sk.magiksoft.sodalis.core.ui.OkCancelDialog
import sk.magiksoft.sodalis.core.utils.UIUtils
import sk.magiksoft.swing.ISTable

import scala.swing.BorderPanel.Position
import scala.swing.FlowPanel.Alignment
import scala.swing._
import scala.swing.Swing._


/**
 * @author wladimiiir 
 * @since 2014/12/03
 */
class ModuleConfigurationDialog(owner: Window, manager: ModuleManager) extends OkCancelDialog(owner, LocaleManager.getString("moduleConfiguration")) {
  private val model = new ModuleTableModel

  initLayuot()

  private def initLayuot(): Unit = {
    def createAddAction(): Action = {
      val action = Action("") {

      }
      action.icon = IconFactory.getInstance().getIcon("add")
      action
    }

    def createRemoveAction(): Action = {
      val action = Action("") {

      }
      action.icon = IconFactory.getInstance().getIcon("remove")
      action
    }

    def createMoveUpAction(): Action = {
      val action = Action("") {

      }
      action.icon = IconFactory.getInstance().getIcon("arrowUp")
      action
    }

    def createMoveDownAction(): Action = {
      val action = Action("") {

      }
      action.icon = IconFactory.getInstance().getIcon("arrowDown")
      action
    }

    def createToolbar(buttons: Button*): Component = {
      def initToolbarButton(button: Button): Unit = {
        button.opaque = false
        button.borderPainted = false
        button.focusPainted = false
        button.preferredSize = (25, 25)
      }

      val toolBar = UIUtils.createToolBar()
      buttons.foreach(b => {
        initToolbarButton(b)
        toolBar.add(b.peer)
      })
      Component.wrap(toolBar)
    }

    setMainPanel(new BorderPanel {
      add(createToolbar(
        new Button(createAddAction()),
        new Button(createRemoveAction()),
        new Button(createMoveUpAction()),
        new Button(createMoveDownAction())
      ), Position.North)
      add(new ScrollPane(Component.wrap(new ISTable(model))), Position.Center)
    }.peer)
  }


  setSize(800, 600)
  setLocationRelativeTo(getOwner)
}
