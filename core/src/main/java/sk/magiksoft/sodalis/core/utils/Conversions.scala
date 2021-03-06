package sk.magiksoft.sodalis.core.utils

import javax.swing.{Icon, JComponent}
import scala.swing.{Action, Component}
import swing.event.{MousePressed, MouseClicked, MouseEvent}

/**
 * @author wladimiiir
 * @since 2010/4/22
 */

object Conversions {
  implicit def toScalaComponent(c: JComponent): Component = Component.wrap(c)

  implicit def toJavaComponent(c: Component): JComponent = c.peer

  implicit def toInt(d: Double): Int = d.asInstanceOf[Int]

  implicit def toMouseEvent(e: java.awt.event.MouseEvent): MouseEvent = {
    val c: Component = e.getSource match {
      case jc: JComponent => jc
      case _ => null
    }

    e.getID match {
      case java.awt.event.MouseEvent.MOUSE_CLICKED => new MouseClicked(c, e.getPoint, e.getModifiers, e.getClickCount, e.isPopupTrigger)(e)
      case java.awt.event.MouseEvent.MOUSE_PRESSED => new MousePressed(c, e.getPoint, e.getModifiers, e.getClickCount, e.isPopupTrigger)(e)
    }
  }

  implicit def toScalaAction(a: javax.swing.Action): scala.swing.Action = {
    val action = new Action(a.getValue(javax.swing.Action.NAME) match {
      case name: String => name
      case _ => null
    }) {
      override lazy val peer = a

      def apply() {
        a.actionPerformed(null)
      }
    }
    action.icon = a.getValue(javax.swing.Action.SMALL_ICON).asInstanceOf[Icon]
    action
  }

  class SubObject[T <: AnyRef](x: T) {
    def subClass: Class[_ <: T] = x.getClass.asInstanceOf[Class[T]]

    def subObject = x
  }

  implicit def toSubObject[T <: AnyRef](x: T): SubObject[T] = new SubObject(x)
}
