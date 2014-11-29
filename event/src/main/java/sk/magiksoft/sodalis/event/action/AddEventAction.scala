package sk.magiksoft.sodalis.event.action

import java.awt.event.ActionEvent
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.ui.controlpanel.InfoPanel
import sk.magiksoft.sodalis.core.utils.Conversions._
import sk.magiksoft.sodalis.event.ui.EventControlPanel
import sk.magiksoft.sodalis.core.factory.{EntityFactory, IconFactory}
import sk.magiksoft.sodalis.event.settings.EventSettings
import sk.magiksoft.sodalis.event.data.EventDataManager
import sk.magiksoft.sodalis.event.entity.{EventType, Event}
import collection.JavaConversions._
import java.util.{List, Calendar}
import sk.magiksoft.sodalis.core.action.{ActionMessage, MessageAction}
import sk.magiksoft.sodalis.core.ui.wizard.{WizardFinished, Page, Wizard}
import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.core.settings.Settings
import sk.magiksoft.sodalis.category.CategoryDataManager

/**
 * @author wladimiiir
 * @since 2011/2/18
 */
class AddEventAction extends MessageAction(null, IconFactory.getInstance.getIcon("add")) {
  private var event: Event = _
  private var wizard: Option[Wizard] = None

  private val pages = {
    val infoPanels = new EventControlPanel().getAllInfoPanels.filter(_.isWizardSupported)
    infoPanels.map {
      ip => new EventPage(infoPanels.indexOf(ip), ip)
    }
  }

  def getActionMessage(objects: List[_]) = new ActionMessage(true, LocaleManager.getString("addEvent"))


  def actionPerformed(e: ActionEvent) = {
    val wizard = this.wizard match {
      case Some(wizard) => wizard
      case None => createWizard
    }
    event = createEvent
    event.getEndTime.add(Calendar.MINUTE, EventSettings.getInstance.getInt(EventSettings.I_EVENT_DURATION))
    event.setEventType(EventDataManager.getInstance.getDatabaseEntities(classOf[EventType])(0))
    for (page <- pages) {
      page.infoPanel.setupPanel(event)
      page.infoPanel.initData
    }
    wizard.showWizard
  }

  private def createWizard = {
    val wizard = new Wizard(SodalisApplication.get.getMainFrame, LocaleManager.getString("addEvent"), pages(0))
    wizard.setSize(740, 300)
    wizard.setLocationRelativeTo(null)
    wizard.reactions += {
      case WizardFinished(_) => {
        pages(0).infoPanel.setupObject(event)
        pages.subList(1, pages.size).filter {
          _.infoPanel.acceptObject(event)
        }.foreach {
          _.infoPanel.setupObject(event)
        }
        EventDataManager.getInstance.addDatabaseEntity(event)
      }
    }
    this.wizard = Option(wizard)
    pages.foreach {
      _.infoPanel.initLayout
    }
    wizard
  }

  private def createEvent = {
    val ids = EventSettings.getInstance.getValue(Settings.O_SELECTED_CATEGORIES).asInstanceOf[List[java.lang.Long]]
    val categories = CategoryDataManager.getInstance.getCategories(ids)
    val event = EntityFactory.getInstance.createEntity(classOf[Event])
    event.setCategories(categories)
    pages(0).infoPanel.setupObject(event)
    event
  }

  private class EventPage(val index: Int, val infoPanel: InfoPanel) extends Page {
    def getComponent = infoPanel.getComponentPanel

    def getNextPage = pages.subList(index + 1, pages.size).find {
      _.infoPanel.acceptObject(event)
    }

    def getPreviousPage = pages.subList(0, index).reverse.find {
      _.infoPanel.acceptObject(event)
    }
  }

}
