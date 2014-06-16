
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.form.ui

import java.awt.{Graphics2D, Color}
import java.awt.geom.Rectangle2D
import sk.magiksoft.sodalis.core.utils.Conversions._
import org.jhotdraw.draw.DefaultDrawingView

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Aug 27, 2010
 * Time: 11:12:14 AM
 * To change this template use File | Settings | File Templates.
 */

class FormDrawingView extends DefaultDrawingView {
  var drawingEditor: Option[DrawingEditor] = None

  setBackground(new Color(152, 152, 120))

  override def getDrawingArea = new Rectangle2D.Double(0, 0, getDrawing.get(AttributeKeys.CANVAS_WIDTH), getDrawing.get(AttributeKeys.CANVAS_HEIGHT))

  override def drawCanvas(g: Graphics2D) = {
    super.drawCanvas(g)
    drawingEditor match {
      case Some(drawingEditor) => if (drawingEditor.getActiveView == this) {
        g.setColor(Color.BLUE.darker)
        g.drawRect(0, 0, getDrawingArea.width - 1, getDrawingArea.height - 1)
      }
      case None =>
    }
  }

}