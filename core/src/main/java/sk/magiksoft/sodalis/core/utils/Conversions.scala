
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.core.utils

import javax.swing.JComponent
import swing.Component
import java.awt.Dimension
import swing.event.{MousePressed, MouseClicked, MouseEvent}

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Apr 22, 2010
 * Time: 12:41:12 PM
 * To change this template use File | Settings | File Templates.
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

  implicit def toScalaAction(a: javax.swing.Action) = new scala.swing.Action(a.getValue(javax.swing.Action.NAME) match {
    case name: String => name
    case _ => null
  }) {
    override lazy val peer = a

    def apply() {
      a.actionPerformed(null)
    }
  }

  class SubObject[T <: AnyRef](x: T) {
    def subClass: Class[_ <: T] = x.getClass.asInstanceOf[Class[T]]
    def subObject = x
  }

  implicit def toSubObject[T <: AnyRef](x: T) = new SubObject(x)
}