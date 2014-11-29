package sk.magiksoft.sodalis.item.ui

import sk.magiksoft.sodalis.item.entity.{ItemPropertyValue, Item}
import java.awt.BorderLayout
import javax.swing.JPanel
import scala.collection.mutable.ListBuffer
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel

/**
 * @author wladimiiir
 * @since 2010/6/21
 */

class ItemInfoPanel extends AbstractInfoPanel {
  private var item: Option[Item] = None
  private lazy val presenterPanel = new ItemTypePresenterPanel

  def createLayout = {
    val layoutPanel = new JPanel(new BorderLayout)

    layoutPanel.add(presenterPanel.peer, BorderLayout.CENTER)
    presenterPanel.addChangeListener(() => {
      super.fireEditing
    })
    layoutPanel
  }

  def initData {
    item match {
      case Some(item) if !initialized => {
        presenterPanel.setValues(item.values.toList)
        initialized = true
      }
      case _ =>
    }
  }

  def reloadPresenterPanel = {
    presenterPanel.reload(item.get.itemType)
  }

  def setupPanel(entity: Any) = entity match {
    case i: Item => {

      val reload = item match {
        case Some(item) => item.itemType.id != i.itemType.id
        case None => true
      }
      item = new Some(i)
      if (reload) {
        reloadPresenterPanel
      }
      initialized = false
    }

    case _ =>
  }

  def setupObject(entity: Any) {
    entity match {
      case item: Item if initialized => {
        item.values = new ListBuffer[ItemPropertyValue] ++ presenterPanel.getValues
      }
      case _ =>
    }
  }

  def getPanelName = LocaleManager.getString("basicInfo")
}
