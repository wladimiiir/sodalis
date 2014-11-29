package sk.magiksoft.sodalis.item.ui

import sk.magiksoft.sodalis.item.presenter.Presenter
import java.awt.Insets
import collection._
import sk.magiksoft.sodalis.item.entity.{ItemPropertyValue, ItemType}
import scala.swing.{Label, GridBagPanel, Component}
import scala.collection.mutable.ListBuffer

/**
 * @author wladimiiir
 * @since 2010/6/10
 */

class ItemTypePresenterPanel extends GridBagPanel {
  private val itemPropertyMap = mutable.Map.empty[Long, Tuple2[Presenter, Component]]
  private val listenerList = new ListBuffer[() => Unit]
  private val listener = () => {
    for (l <- listenerList) {
      l()
    }
  }

  def reload(itemType: ItemType) = {
    var c = new Constraints

    peer.removeAll
    itemPropertyMap.clear

    itemType match {
      case null =>
      case it: ItemType => {
        for (column <- Range(0, 10)) {
          c.grid = (column * 2, 0)
          for (itemProperty <- it.itemProperties.filter(ip => ip.column == column)) {
            val presenter = Class.forName(itemProperty.presenterClassName.trim).newInstance.asInstanceOf[Presenter]
            val component = presenter.getComponent(itemProperty, null)
            presenter.addChangeListener(component, listener)
            if (itemProperty.getId != null) {
              itemPropertyMap += itemProperty.getId.longValue ->(presenter, component)
            }
            presenter.addName match {
              case true => {
                c.gridwidth = 1
                c.gridheight = itemProperty.rows
                c.weightx = 0.0
                c.weighty = 0.0
                c.anchor = GridBagPanel.Anchor.NorthEast
                c.fill = GridBagPanel.Fill.None
                c.insets = new Insets(7, 3, 0, 0)
                add(new Label(itemProperty.name), c)
                c.gridx += 1
                c.anchor = GridBagPanel.Anchor.NorthEast
                c.fill = GridBagPanel.Fill.Horizontal
                c.insets = new Insets(3, 5, 0, 10)
                add(component, c)
              }
              case false => {
                c.gridwidth = 2
                c.gridheight = itemProperty.rows
                c.insets = new Insets(3, 3, 3, 10)
                c.anchor = GridBagPanel.Anchor.NorthEast
                c.fill = GridBagPanel.Fill.Horizontal
                add(component, c)
              }
            }

            c.gridx = column * 2
            c.gridy += itemProperty.rows
          }
        }
      }
    }
    revalidate
    repaint
  }

  def addChangeListener(listener: () => Unit) {
    listenerList += listener
  }

  def clear = {
    for (element <- itemPropertyMap.toList) {
      element._2._1.setValue(element._2._2, null)
    }
  }

  def setValues(values: List[ItemPropertyValue]) = {
    for (element <- itemPropertyMap.toList) {
      values.find(value => value.itemPropertyID.equals(element._1)) match {
        case Some(value) => element._2._1.setValue(element._2._2, value.value)
        case None => element._2._1.setValue(element._2._2, null)
      }
    }
  }

  def getValues =
    itemPropertyMap.toList.map {
      id => {
        val value = new ItemPropertyValue
        value.itemPropertyID = id._1
        value.value = id._2._1.getValue(id._2._2)
        value
      }
    }
}

object ItemTypePresenterPanel {
  def apply(itemType: ItemType) = {
    val presenterPanel = new ItemTypePresenterPanel
    presenterPanel.reload(itemType)
    presenterPanel
  }
}
