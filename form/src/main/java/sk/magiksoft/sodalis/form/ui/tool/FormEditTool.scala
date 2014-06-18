
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.form.ui.tool

import java.awt.event.MouseEvent
import org.jhotdraw.draw.tool.{AbstractTool, Tool}
import java.awt.geom.Point2D
import org.jhotdraw.draw.DrawingEditor
import java.awt.Cursor

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Apr 26, 2010
 * Time: 9:50:08 PM
 * To change this template use File | Settings | File Templates.
 */

class FormEditTool extends AbstractTool {
  var tracker: Tool = null

  def mouseDragged(e: MouseEvent) = {}

  override def mousePressed(e: MouseEvent) = {
    e.getClickCount match {
      case 1 => {
        var figure = getView.findFigure(e.getPoint)
        val p: Point2D.Double = viewToDrawing(e.getPoint)
        if (figure != null && figure.isSelectable) {
          var figureTool: Tool = figure.getTool(p)
          if (figureTool == null) {
            figure = getDrawing.findFigureInside(p)
            if (figure != null) {
              figureTool = figure.getTool(p)
            }
          }
          if (figureTool != null) {
            setTracker(figureTool)
            figureTool.mousePressed(e)
          } else {
            setTracker(null)
          }
        } else {
          setTracker(null)
        }
      }
      case _ =>
    }
  }

  protected def setTracker(newTracker: Tool) = {
    if (tracker != null) {
      tracker.deactivate(getEditor)
    }
    tracker = newTracker
    if (tracker != null) {
      tracker.activate(getEditor)
    }
  }


  override def activate(editor: DrawingEditor) = {
    super.activate(editor)
    getView.clearSelection
    getView.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR))
  }

  override def deactivate(editor: DrawingEditor) = {
    getView.setCursor(Cursor.getDefaultCursor)
  }
}