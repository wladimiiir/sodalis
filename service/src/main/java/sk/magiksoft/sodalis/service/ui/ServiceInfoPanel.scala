package sk.magiksoft.sodalis.service.ui

import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.service.entity.Service
import sk.magiksoft.sodalis.common.ui.PriceComponent
import swing._
import java.awt.Insets
import swing.GridBagPanel.{Fill, Anchor}
import Swing._
import sk.magiksoft.sodalis.core.ui.controlpanel.{InfoPanelPublisher, AbstractInfoPanel}

/**
 * @author wladimiiir
 * @since 2011/3/11
 */

class ServiceInfoPanel extends AbstractInfoPanel with InfoPanelPublisher {
  private var service: Option[Service] = None
  private lazy val name = new TextField
  private lazy val code = new TextField
  private lazy val description = new TextArea
  private lazy val price = new PriceComponent

  def createLayout = new GridBagPanel {
    add(new Label {
      text = LocaleManager.getString("serviceName")
    }, new Constraints {
      grid = (0, 0)
      insets = new Insets(5, 5, 0, 0)
      anchor = Anchor.East
    })
    add(ServiceInfoPanel.this.name, new Constraints {
      grid = (1, 0)
      insets = new Insets(5, 3, 0, 5)
      weightx = 1.0
      fill = Fill.Horizontal
      anchor = Anchor.East
    })
    add(new Label {
      text = LocaleManager.getString("serviceCode")
    }, new Constraints {
      grid = (0, 1)
      insets = new Insets(3, 5, 0, 0)
      anchor = Anchor.East
    })
    add(code, new Constraints {
      grid = (1, 1)
      insets = new Insets(3, 3, 0, 5)
      weightx = 1.0
      fill = Fill.Horizontal
      anchor = Anchor.East
    })
    add(new Label {
      text = LocaleManager.getString("serviceDescription")
    }, new Constraints {
      grid = (0, 2)
      insets = new Insets(3, 5, 0, 0)
      anchor = Anchor.NorthEast
    })
    add(new ScrollPane {
      viewportView = Option(description)
      preferredSize = (100, 100)
    }, new Constraints {
      grid = (1, 2)
      insets = new Insets(3, 3, 0, 5)
      weightx = 1.0
      fill = Fill.Horizontal
      anchor = Anchor.East
    })
    add(new Label {
      text = LocaleManager.getString("servicePrice")
    }, new Constraints {
      grid = (0, 3)
      insets = new Insets(3, 5, 8, 0)
      anchor = Anchor.SouthEast
    })
    add(price, new Constraints {
      grid = (1, 3)
      insets = new Insets(3, 3, 5, 5)
      anchor = Anchor.West
    })

    ServiceInfoPanel.this.listenTo(ServiceInfoPanel.this.name, code, description, price)
  }.peer

  def initData() {
    service match {
      case Some(service) if !initialized => {
        name.text = service.name
        code.text = service.code
        description.text = service.description
        price.setPrice(service.price)
        initialized = true
      }
      case _ =>
    }
  }

  def setupPanel(entity: Any) {
    entity match {
      case service: Service => {
        this.service = Option(service)
        initialized = false
      }
      case _ =>
    }
  }

  def setupObject(entity: Any) {
    entity match {
      case service: Service if initialized => {
        service.name = name.text
        service.code = code.text
        service.description = description.text
        price.updatePrice(service.price)
      }
      case _ =>
    }
  }

  def getPanelName = LocaleManager.getString("service")
}
