
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.form.ui.tool

import org.jhotdraw.draw.tool.AbstractTool
import java.awt.event.MouseEvent
import sk.magiksoft.sodalis.core.utils.Conversions._

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Apr 23, 2010
 * Time: 10:31:06 PM
 * To change this template use File | Settings | File Templates.
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