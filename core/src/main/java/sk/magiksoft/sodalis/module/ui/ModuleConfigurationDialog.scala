package sk.magiksoft.sodalis.module.ui

import java.awt.Window
import java.io.File
import java.net.{URL, URLClassLoader}
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

import org.reflections.util.ConfigurationBuilder
import org.reflections.{ReflectionUtils, Reflections}
import sk.magiksoft.sodalis.core.factory.IconFactory
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.module.{DynamicModule, Module, DatabaseModuleManager}
import sk.magiksoft.sodalis.core.ui.OkCancelDialog
import sk.magiksoft.sodalis.core.utils.{FileUtils, UIUtils}
import sk.magiksoft.sodalis.module.entity.ModuleEntity
import sk.magiksoft.swing.{ISFileChooser, ISTable}

import scala.reflect.internal.util.{ScalaClassLoader, AbstractFileClassLoader}
import scala.reflect.io.ZipArchive
import scala.swing.BorderPanel.Position
import scala.swing.Swing._
import scala.swing._
import scala.collection.JavaConversions._
import scala.util.Random


/**
 * @author wladimiiir 
 * @since 2014/12/03
 */
class ModuleConfigurationDialog(owner: Window, manager: DatabaseModuleManager) extends OkCancelDialog(owner, LocaleManager.getString("moduleConfiguration")) {
  private val MODULE_TEMP_DIR: String = "data" + File.separator + "temp" + File.separator + "module"

  private val model = new ModuleTableModel
  private val table = new ISTable(model)
  private val moduleFileChooser = new ISFileChooser(new FileNameExtensionFilter(LocaleManager.getString("sodalisModuleArchive"), "szip", "jar"))
  private val moduleEntities = manager.getModuleEntities.toBuffer

  initLayout()
  reloadModel()

  private def reloadModel(): Unit = {
    model.setObjects(moduleEntities.sortBy(_.order))
  }

  private def getSelectedEntity: Option[ModuleEntity] = table.getSelectedRow match {
    case index: Int if index >= 0 => Option(model.getObject(index))
    case _ => None
  }

  private def loadModuleArchive(file: File) = {
    val moduleDir = new File(MODULE_TEMP_DIR)

    FileUtils.deleteDir(moduleDir)
    FileUtils.unpackZipFile(new File(file.toURI), moduleDir)

    val classLoader = URLClassLoader.newInstance(moduleDir.listFiles().filter(_.getName.endsWith(".jar")).map(_.toURI.toURL))
    val reflections = new Reflections(ConfigurationBuilder.build(classLoader))


    reflections.getSubTypesOf(classOf[Module])
      .filter(_.isAnnotationPresent(classOf[DynamicModule]))
      .foreach { moduleClass =>
      moduleEntities += new ModuleEntity {
        order = moduleEntities.size
        className = moduleClass.getName
      }
    }
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
      action.icon = IconFactory.getInstance().getIcon("add")
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
      action.icon = IconFactory.getInstance().getIcon("remove")
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

          case None =>
        }
      }
      action.icon = IconFactory.getInstance().getIcon("arrowUp")
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

          case None =>
        }
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
      add(new ScrollPane(Component.wrap(table)), Position.Center)
    }.peer)
  }


  setSize(800, 600)
  setLocationRelativeTo(getOwner)
}