package sk.magiksoft.sodalis.item.presenter

import sk.magiksoft.sodalis.item.entity.ItemProperty
import java.io.Serializable
import scala.swing.{TextArea, Component, ScrollPane}
import swing.ScrollPane.BarPolicy
import java.awt.{Color, Dimension}
import javax.swing.{JTextArea, BorderFactory}

/**
 * @author wladimiiir
 * @since 2010/6/18
 */

class TextAreaPresenter extends Presenter {
  def getValue(component: Component) = component.asInstanceOf[ScrollPane].peer.getViewport.getView.asInstanceOf[JTextArea].getText

  def getReadableValue(value: Serializable) = value.asInstanceOf[String]

  def getComponent(itemProperty: ItemProperty, value: Serializable) = {
    val textArea: TextArea = new TextArea {
      border = BorderFactory.createMatteBorder(1, 1, 1, 0, Color.GRAY)
    }
    new ScrollPane(textArea) {
      verticalScrollBarPolicy = BarPolicy.Always
      preferredSize = new Dimension(220, 100)
      border = BorderFactory.createTitledBorder(itemProperty.name)
      listenTo(textArea)
    }
  }

  def getEditorActions(itemProperty: ItemProperty, reloadAction: => Unit) = Nil

  def setValue(c: Component, value: Serializable) = {
    c.asInstanceOf[ScrollPane].peer.getViewport.getView.asInstanceOf[JTextArea].setText(value match {
      case t: String => t
      case _ => null
    })
  }

  def addName = false

  def addChangeListener(component: Component, listener: () => Unit) = component.reactions += {
    case scala.swing.event.ValueChanged(_) => listener()
  }
}
