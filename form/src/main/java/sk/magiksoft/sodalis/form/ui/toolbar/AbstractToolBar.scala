
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.form.ui.toolbar

import org.jhotdraw.gui.JDisclosureToolBar
import org.jhotdraw.draw.DrawingEditor
import scala.math._
import scala.swing.{Component, GridPanel, Panel}
import sk.magiksoft.sodalis.core.utils.Conversions._

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: May 7, 2010
 * Time: 2:38:57 PM
 * To change this template use File | Settings | File Templates.
 */

abstract class AbstractToolBar(name: String, drawingEditor: DrawingEditor, buttonItems: List[(String, Any)]) extends JDisclosureToolBar {
  var panels = new Array[Panel](1)
  var rowCount = 4

  setOpaque(false)
  setDisclosureStateCount(2)
  setName(name)

  override def getDisclosedComponent(state: Int) = {
    val columnCount = ceil(buttonItems.size + 1 / rowCount).intValue

    state match {
      case 1 => panels(0) match {
        case (p: Panel) => p
        case _ => {
          panels(0) = new GridPanel(rowCount, columnCount) {
            vGap = 1
            hGap = 1
            opaque = false
            for (item <- buttonItems) {
              var button = createComponent(item)
              if (button != null) {
                contents += button
              }
            }
          }
          panels(0)
        }
      }
      case _ => null
    }
  }

  protected def createComponent(item: (String, Any)): Component
}