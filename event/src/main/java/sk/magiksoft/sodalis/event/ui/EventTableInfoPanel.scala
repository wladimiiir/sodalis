package sk.magiksoft.sodalis.event.ui

import sk.magiksoft.sodalis.event.entity.Event
import javax.swing.JScrollPane
import java.awt.event.{MouseEvent, MouseAdapter}
import sk.magiksoft.sodalis.event.EventModule
import sk.magiksoft.sodalis.core.action.GoToEntityAction
import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel
import sk.magiksoft.sodalis.core.entity.DatabaseEntityContainer
import sk.magiksoft.swing.ISTable
import sk.magiksoft.sodalis.core.factory.ColorList
import sk.magiksoft.sodalis.core.locale.LocaleManager

/**
 * @author wladimiiir
 * @since 2010/11/30
 */

class EventTableInfoPanel extends AbstractInfoPanel {
  private val tableModel = new EventTableModel
  private val goToEntity = new GoToEntityAction(classOf[EventModule])
  private var currentObject: Option[DatabaseEntityContainer] = None

  def createLayout = new JScrollPane(new ISTable(tableModel) {
    addMouseListener(new MouseAdapter {
      override def mouseClicked(e: MouseEvent) = e.getClickCount match {
        case 2 => rowAtPoint(e.getPoint) match {
          case -1 =>
          case row: Int => goToEntity.goTo(tableModel.getObject(row))
        }
        case _ =>
      }
    })
  }) {
    getViewport.setBackground(ColorList.SCROLLPANE_BACKGROUND)
    setPreferredSize(new java.awt.Dimension(100, 100))
  }


  override def isWizardSupported = false

  def initData = initialized match {
    case true =>
    case false => {
      currentObject match {
        case Some(container) => {
          val events = container.getDatabaseEntities(classOf[Event])
          tableModel.setObjects(container.getDatabaseEntities(classOf[Event]))
        }
        case None => tableModel.removeAllObjects
      }
      initialized = true
    }
  }

  def setupPanel(entity: Any) = {
    currentObject = entity match {
      case container: DatabaseEntityContainer => Option(container)
      case None => None
    }
    initialized = false
  }

  def setupObject(entity: Any) = {
  }

  def getPanelName = LocaleManager.getString("events")
}
