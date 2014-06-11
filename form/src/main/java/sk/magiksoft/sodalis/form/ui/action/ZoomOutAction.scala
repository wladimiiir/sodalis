
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.form.ui.action

import java.awt.event.ActionEvent
import org.jhotdraw.draw.action.AbstractDrawingEditorAction
import sk.magiksoft.sodalis.core.factory.IconFactory
import javax.swing.Action
import org.jhotdraw.draw.DrawingEditor


/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: May 9, 2010
 * Time: 6:10:31 PM
 * To change this template use File | Settings | File Templates.
 */

class ZoomOutAction(editor: DrawingEditor) extends AbstractDrawingEditorAction(editor) {
  var scaleMultiplier = 1.1;

  putValue(Action.SMALL_ICON, IconFactory.getInstance.getIcon("zoomOut"))
  putValue(Action.NAME, "")

  def actionPerformed(e: ActionEvent): Unit = {
    if (getView == null) {
      return
    }

    var scaleFactor = getView.getScaleFactor / scaleMultiplier
    var iterator = editor.getDrawingViews.iterator
    while (iterator.hasNext) {
      iterator.next.setScaleFactor(scaleFactor)
    }
  }
}