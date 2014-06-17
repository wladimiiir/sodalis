
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.item.presenter

import java.io.Serializable
import sk.magiksoft.sodalis.item.entity.ItemProperty
import scala.swing.{Component, Action}

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: May 30, 2010
 * Time: 11:01:20 AM
 * To change this template use File | Settings | File Templates.
 */

trait Presenter {
  def addName: Boolean

  def setValue(c: Component, value: Serializable): Unit

  def getEditorActions(itemProperty: ItemProperty, reloadAction: => Unit): List[Action]

  def getComponent(itemProperty: ItemProperty, value: Serializable): Component

  def getValue(component: Component): Serializable

  def getReadableValue(value: Serializable): String

  def addChangeListener(component: Component, listener: () => Unit): Unit
}