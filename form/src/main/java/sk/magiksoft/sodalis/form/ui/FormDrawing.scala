package sk.magiksoft.sodalis.form.ui

import org.jhotdraw.draw.{DefaultDrawing, AttributeKeys}
import javax.swing.event.EventListenerList

/**
 * @author wladimiiir
 * @since 2010/8/27
 */
@SerialVersionUID(-3529950674630805270l)
class FormDrawing(val width: Int, val height: Int) extends DefaultDrawing {
  set(AttributeKeys.CANVAS_WIDTH, new java.lang.Double(width))
  set(AttributeKeys.CANVAS_HEIGHT, new java.lang.Double(height))

  def clear() = {
    listenerList = new EventListenerList
  }
}
