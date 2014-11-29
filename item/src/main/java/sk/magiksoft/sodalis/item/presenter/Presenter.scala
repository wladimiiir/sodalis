package sk.magiksoft.sodalis.item.presenter

import java.io.Serializable
import sk.magiksoft.sodalis.item.entity.ItemProperty
import scala.swing.{Component, Action}

/**
 * @author wladimiiir
 * @since 2010/5/30
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
