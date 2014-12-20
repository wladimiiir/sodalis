package sk.magiksoft.sodalis.person.action

import sk.magiksoft.sodalis.icon.IconManager
import sk.magiksoft.sodalis.person.ui.AbstractPersonContext
import sk.magiksoft.wizard.Wizard
import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.core.ui.controlpanel.InfoPanel
import sk.magiksoft.sodalis.core.utils.UIUtils
import sk.magiksoft.sodalis.person.entity.Person
import sk.magiksoft.sodalis.core.data.DefaultDataManager
import sk.magiksoft.sodalis.category.CategoryDataManager
import java.awt.event.ActionEvent
import sk.magiksoft.sodalis.core.factory.EntityFactory
import sk.magiksoft.sodalis.core.settings.Settings
import sk.magiksoft.sodalis.core.action.{EntityAction, ActionMessage, MessageAction}
import java.util.{List => jList}

/**
 * @author wladimiiir
 * @since 2010/12/16
 */

abstract class AddPersonAbstractAction(personContext: AbstractPersonContext)
  extends MessageAction(IconManager.getInstance.getIcon("add")) with EntityAction[Person] {

  private var wizard: Option[Wizard] = None
  private lazy val infoPanels = createInfoPanels

  def getActionMessage(objects: jList[_]) = new ActionMessage(true, getActionName)

  protected def getActionName: String

  protected def getWizardTitle: String

  def getName(entities: List[Person]) = getActionName

  def isAllowed(entities: List[Person]) = true

  def actionPerformed(e: ActionEvent) {
    apply(Nil)
  }

  def apply(entities: List[Person]) {
    wizard match {
      case Some(wizard) =>
      case None => {
        wizard = Option(createWizard)
      }
    }

    val person = EntityFactory.getInstance.createEntity(classOf[Person])
    setupPerson(person)
    for (infoPanel <- infoPanels) {
      infoPanel.setupPanel(person)
      infoPanel.initData
    }

    wizard.get.showWizard match {
      case Wizard.FINISH_ACTION => {
        for (infoPanel <- infoPanels) {
          infoPanel.setupObject(person)
        }
        DefaultDataManager.getInstance.addDatabaseEntity(person)
      }
      case _ =>
    }
  }

  protected def setupPerson(person: Person) {
    val ids = personContext.getSettings.getValue(Settings.O_SELECTED_CATEGORIES).asInstanceOf[jList[java.lang.Long]]
    val categories = CategoryDataManager.getInstance.getCategories(ids)

    person.setCategories(categories)
  }

  protected def createInfoPanels: Array[InfoPanel]

  private def createWizard = {
    infoPanels.foreach(_.initLayout)
    new Wizard(SodalisApplication.get.getMainFrame, infoPanels.map {
      _.getComponentPanel
    }) {
      setTitle(getWizardTitle)
      UIUtils.makeISDialog(this)
      setSize(640, 380)
      setLocationRelativeTo(null)
    }
  }
}
