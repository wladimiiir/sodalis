
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.item.ui

import sk.magiksoft.sodalis.item.entity.{ItemPropertyValue, Item}
import java.awt.BorderLayout
import javax.swing.JPanel

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jun 21, 2010
 * Time: 11:40:22 AM
 * To change this template use File | Settings | File Templates.
 */

class ItemInfoPanel extends AbstractInfoPanel {
  private var item: Option[Item] = None
  private lazy val presenterPanel = new ItemTypePresenterPanel

  def createLayout = {
    val layoutPanel = new JPanel(new BorderLayout)

    layoutPanel.add(presenterPanel, BorderLayout.CENTER)
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