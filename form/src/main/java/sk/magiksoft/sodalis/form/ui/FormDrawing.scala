
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.form.ui

import org.jhotdraw.draw.{DefaultDrawing, AttributeKeys}
import javax.swing.event.EventListenerList

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Aug 27, 2010
 * Time: 11:15:28 AM
 * To change this template use File | Settings | File Templates.
 */
@SerialVersionUID(-3529950674630805270l)
class FormDrawing(val width: Int, val height: Int) extends DefaultDrawing {
  set(AttributeKeys.CANVAS_WIDTH, new java.lang.Double(width))
  set(AttributeKeys.CANVAS_HEIGHT, new java.lang.Double(height))

  def clear() = {
    listenerList = new EventListenerList
  }
}