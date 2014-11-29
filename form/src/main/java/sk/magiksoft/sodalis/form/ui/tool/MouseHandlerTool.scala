package sk.magiksoft.sodalis.form.ui.tool

import org.jhotdraw.draw.tool.AbstractTool
import java.awt.event.MouseEvent
import sk.magiksoft.sodalis.core.utils.Conversions._

/**
 * @author wladimiiir
 * @since 2010/4/23
 */

class MouseHandlerTool(handler: MouseHandler) extends AbstractTool {
  override def mousePressed(evt: MouseEvent) = {
    handler.handleMousePressed(evt)
  }

  override def mouseClicked(evt: MouseEvent) = {
    handler.handleMouseClicked(evt)
  }

  def mouseDragged(e: MouseEvent) = {}


}
