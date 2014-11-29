package sk.magiksoft.sodalis.item.presenter

import sk.magiksoft.sodalis.item.entity.ItemProperty
import java.io.Serializable
import scala.swing.{Component, TextField}
import java.awt.Dimension
import java.lang.String
import swing.event.ValueChanged

/**
 * @author wladimiiir
 * @since 2010/5/30
 */

class TextFieldPresenter extends Presenter {
  override def getComponent(itemProperty: ItemProperty, value: Serializable): Component = new TextField {
    text = value.asInstanceOf[String]
    preferredSize = new Dimension(220, 21)
  }

  def getValue(component: Component): Serializable =
    component.asInstanceOf[TextField].text

  def getReadableValue(value: Serializable) = value.asInstanceOf[String]

  def getEditorActions(itemProperty: ItemProperty, reloadAction: => Unit) = Nil

  def setValue(c: Component, value: Serializable) = c.asInstanceOf[TextField].text = value match {
    case t: String => t
    case _ => null
  }

  def addName = true

  def addChangeListener(component: Component, listener: () => Unit) = {
    component.reactions += {
      case ValueChanged(_) => listener()
    }
  }
}
