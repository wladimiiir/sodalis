package sk.magiksoft.sodalis.form.ui.action

import java.awt.event.ActionEvent
import javax.swing.Action
import sk.magiksoft.sodalis.core.factory.IconFactory
import org.jhotdraw.draw.action.AbstractDrawingEditorAction
import org.jhotdraw.draw.DrawingEditor


/**
 * @author wladimiiir
 * @since 2010/5/9
 */

class ZoomOutAction(editor: DrawingEditor) extends AbstractDrawingEditorAction(editor) {
  var scaleMultiplier = 1.1;

  putValue(Action.SMALL_ICON, IconFactory.getInstance.getIcon("zoomOut"))
  putValue(Action.NAME, "")

  def actionPerformed(e: ActionEvent): Unit = {
    if (getView == null) {
      return
    }

    val scaleFactor = getView.getScaleFactor / scaleMultiplier
    val iterator = editor.getDrawingViews.iterator
    while (iterator.hasNext) {
      iterator.next.setScaleFactor(scaleFactor)
    }
  }
}
