
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.item.presenter

import sk.magiksoft.sodalis.item.entity.ItemProperty
import java.io.Serializable
import swing.TextField
import java.awt.Dimension
import java.lang.String
import swing.event.ValueChanged

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: May 30, 2010
 * Time: 11:09:45 AM
 * To change this template use File | Settings | File Templates.
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