package sk.magiksoft.sodalis.module.ui

import java.awt
import java.awt.event.{ActionEvent, ActionListener}
import java.awt.{Font, Window}
import java.io.File
import java.net.URLClassLoader
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.{JFileChooser, JLabel, JTable}

import org.reflections.Reflections
import org.reflections.util.ConfigurationBuilder
import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.module.{DatabaseModuleManager, VisibleModule, Module}
import sk.magiksoft.sodalis.core.ui.OkCancelDialog
import sk.magiksoft.sodalis.core.utils.{FileUtils, UIUtils}
import sk.magiksoft.sodalis.icon.IconManager
import sk.magiksoft.sodalis.module.ModuleLoader
import sk.magiksoft.sodalis.module.entity.ModuleEntity
import sk.magiksoft.swing.{ISFileChooser, ISTable}

import scala.collection.JavaConversions._
import scala.swing.BorderPanel.Position
import scala.swing.Swing._
import scala.swing._


/**
 * @author wladimiiir 
 * @since 2014/12/03
 */
class ModuleConfigurationDialog(owner: Window, manager: DatabaseModuleManager) extends OkCancelDialog(owner, LocaleManager.getString("moduleConfiguration")) {
  private val model = new ModuleTableModel
  private val table = new ISTable(model) {
    columnModel.getColumn(0).setCellRenderer(new DefaultTableCellRenderer {
      override def getTableCellRendererComponent(table: JTable, value: scala.Any, isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int): awt.Component = {
        val label = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column).asInstanceOf[JLabel]
        if (moduleEntities(row).id == null) {
          label.setFont(label.getFont.deriveFont(Font.BOLD))
        }
        label
      }
    })
  }
  private val moduleFileChooser = new ISFileChooser(new FileNameExtensionFilter(LocaleManager.getString("sodalisModuleArchive"), "szip", "jar"))
  private val moduleEntities = manager.getModuleEntities.toBuffer

  initLayout()
  reloadModel()
  getOkButton.addActionListener(new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit = {
      moduleEntities.filter(_.id == null).foreach(newModuleEntity => {
        manager.addModule(newModuleEntity)
      })
      moduleEntities.filter(_.id != null).foreach(updatedModuleEntity => {
        manager.updateModule(updatedModuleEntity)
      })
      SodalisApplication.get().showMessage(LocaleManager.getString("restartNeeded"))
    }
  })


  private def reloadModel(): Unit = {
    model.setObjects(moduleEntities.sortBy(_.order))
  }

  private def getSelectedEntity: Option[ModuleEntity] = table.getSelectedRow match {
    case index: Int if index >= 0 => Option(model.getObject(index))
    case _ => None
  }

  private def loadModuleArchive(file: File) = {
    moduleEntities ++= ModuleLoader.loadModules(file)
  }

  private def initLayout(): Unit = {
    def createAddAction(): Action = {
      val action = Action("") {
        moduleFileChooser.showOpenDialog(owner) match {
          case JFileChooser.APPROVE_OPTION =>
            loadModuleArchive(moduleFileChooser.getSelectedFile)

          case _ =>
        }

        reloadModel()
        table.setRowSelectionInterval(moduleEntities.size - 1, moduleEntities.size - 1)
      }
      action.icon = IconManager.getInstance().getIcon("add")
      action
    }

    def createRemoveAction(): Action = {
      val action = Action("") {
        getSelectedEntity match {
          case Some(entity) =>
            moduleEntities -= entity
            moduleEntities.filter(_.order > entity.order).foreach(_.order -= 1)
            reloadModel()

          case None =>
        }
      }
      action.icon = IconManager.getInstance().getIcon("remove")
      action
    }

    def createMoveUpAction(): Action = {
      val action = Action("") {
        getSelectedEntity match {
          case Some(entity) if entity.order > 0 =>
            entity.order -= 1
            moduleEntities.filter(_ != entity).find(_.order == entity.order) match {
              case Some(otherEntity) => otherEntity.order += 1
              case None =>
            }
            reloadModel()
            table.setRowSelectionInterval(entity.order, entity.order)

          case _ =>
        }
      }
      action.icon = IconManager.getInstance().getIcon("arrowUp")
      action
    }

    def createMoveDownAction(): Action = {
      val action = Action("") {
        getSelectedEntity match {
          case Some(entity) if entity.order < moduleEntities.size - 1 =>
            entity.order += 1
            moduleEntities.filter(_ != entity).find(_.order == entity.order) match {
              case Some(otherEntity) => otherEntity.order -= 1
              case None =>
            }
            reloadModel()
            table.setRowSelectionInterval(entity.order, entity.order)

          case _ =>
        }
      }
      action.icon = IconManager.getInstance().getIcon("arrowDown")
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
      add(new ScrollPane(Component.wrap(table)), Position.Center)
    }.peer)
  }


  setSize(800, 600)
  setLocationRelativeTo(getOwner)
}
