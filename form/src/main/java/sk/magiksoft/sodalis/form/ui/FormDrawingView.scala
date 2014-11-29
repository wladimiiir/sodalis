package sk.magiksoft.sodalis.form.ui

import java.awt.{Graphics2D, Color}
import java.awt.geom.Rectangle2D
import sk.magiksoft.sodalis.core.utils.Conversions._
import org.jhotdraw.draw.{AttributeKeys, DrawingEditor, DefaultDrawingView}

/**
 * @author wladimiiir
 * @since 2010/8/27
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
