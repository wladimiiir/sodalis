
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.event.ui

import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.entity.DatabaseEntityContainer
import scala.collection.JavaConversions._
import sk.magiksoft.sodalis.event.entity.Event
import collection.mutable.Buffer
import sk.magiksoft.swing.ISTable
import javax.swing.{JScrollPane, JPanel}
import sk.magiksoft.sodalis.core.factory.ColorList
import java.awt.Dimension
import swing.Swing
import java.awt.event.{MouseEvent, MouseAdapter}
import sk.magiksoft.sodalis.core.action.GoToEntityAction
import sk.magiksoft.sodalis.event.EventModule

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 11/30/10
 * Time: 11:15 PM
 * To change this template use File | Settings | File Templates.
 */

class EventTableInfoPanel extends AbstractInfoPanel {
  private val tableModel = new EventTableModel
  private val goToEntity = new GoToEntityAction(classOf[EventModule])
  private var currentObject: Option[DatabaseEntityContainer] = None

  def createLayout = new JScrollPane(new ISTable(tableModel){
    addMouseListener(new MouseAdapter{
      override def mouseClicked(e: MouseEvent) = e.getClickCount match {
        case 2 => rowAtPoint(e.getPoint) match {
          case -1 =>
          case row: Int => goToEntity.goTo(tableModel.getObject(row))
        }
        case _ =>
      }
    })
  }){
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
      case container:DatabaseEntityContainer => Option(container)
      case None => None
    }
    initialized = false
  }

  def setupObject(entity: Any) = {
  }

  def getPanelName = LocaleManager.getString("events")
}