package sk.magiksoft.sodalis.service.action

import java.awt.event.ActionEvent
import java.util.List
import sk.magiksoft.sodalis.core.action.{MessageAction, ActionMessage}
import sk.magiksoft.sodalis.service.ui.ServiceInfoPanel
import sk.magiksoft.sodalis.service.entity.Service
import sk.magiksoft.sodalis.core.factory.{IconFactory, EntityFactory}
import sk.magiksoft.sodalis.service.data.ServiceDataManager
import sk.magiksoft.sodalis.settings.ServiceSettings
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.core.ui.OkCancelDialog
import sk.magiksoft.sodalis.category.CategoryDataManager
import sk.magiksoft.sodalis.core.settings.Settings
import scala.collection.JavaConversions._
import java.util

/**
 * @author wladimiiir
 * @since 2011/3/10
 */

class AddServiceAction extends MessageAction(IconFactory.getInstance.getIcon("add")) {
  private lazy val infoPanel = new ServiceInfoPanel {
    initLayout
  }
  private lazy val serviceDialog = new OkCancelDialog(SodalisApplication.get.getMainFrame, LocaleManager.getString("addService")) {
    setMainPanel(infoPanel.getComponentPanel)
    setModal(true)
    setSize(400, 300)
    setLocationRelativeTo(null)
  }

  def getActionMessage(objects: util.List[_]) = new ActionMessage(true, LocaleManager.getString("addService"))

  def actionPerformed(e: ActionEvent) {
    val service = EntityFactory.getInstance.createEntity(classOf[Service])

    infoPanel.setupPanel(service)
    infoPanel.initData
    serviceDialog.setVisible(true)
    serviceDialog.getResultAction match {
      case OkCancelDialog.ACTION_OK => {
        infoPanel.setupObject(service)
        service.categories ++= CategoryDataManager.getInstance.getCategories(ServiceSettings.getValue(Settings.O_SELECTED_CATEGORIES).asInstanceOf[List[java.lang.Long]])
        ServiceDataManager.addDatabaseEntity(service)
      }
      case _ =>
    }
  }
}
