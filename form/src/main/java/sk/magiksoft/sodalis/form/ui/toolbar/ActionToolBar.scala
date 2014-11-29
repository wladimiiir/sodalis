package sk.magiksoft.sodalis.form.ui.toolbar

import sk.magiksoft.sodalis.core.utils.Conversions._
import java.lang.String
import sk.magiksoft.sodalis.form.ui.action.{ZoomInAction, ZoomOutAction}
import sk.magiksoft.sodalis.form.FormContextManager
import sk.magiksoft.sodalis.form.locale.FormResourceBundleUtil
import java.awt.Rectangle
import javax.swing.{JMenuItem, JToolBar, Action, AbstractButton}
import org.jhotdraw.draw.action.{ToggleGridAction, ButtonFactory, AbstractDrawingViewAction}
import scala.swing.{Button, TextField, Component}
import swing.Swing._
import java.beans.{PropertyChangeEvent, PropertyChangeListener}
import org.jhotdraw.draw.{GridConstrainer, AttributeKeys, DrawingEditor}
import org.jhotdraw.gui.plaf.palette.PaletteButtonUI
import org.jhotdraw.draw.event.DrawingComponentRepainter
import scala.swing.event.ValueChanged
import sk.magiksoft.sodalis.core.locale.LocaleManager

/**
 * @author wladimiiir
 * @since 2010/5/9
 */

class ActionToolBar(drawingEditor: DrawingEditor, items: List[(String, Any)]) extends AbstractToolBar(LocaleManager.getString("actions"), drawingEditor, items) {
  def this(drawingEditor: DrawingEditor) = this(drawingEditor, ActionToolBar.createButtons(drawingEditor))

  protected def createComponent(item: (String, Any)) = {
    item._2 match {
      case action: Action => {
        val button = new Button(action) {
          text = null
          preferredSize = (22, 22)
        }
        button.peer.setUI(PaletteButtonUI.createUI(button).asInstanceOf[PaletteButtonUI])
        button.peer.setHideActionText(true)
        button
      }
      case button: AbstractButton => setupButton(button)
      case component: Component => component
      case _ => new JToolBar.Separator
    }
  }

  protected def setupButton(button: AbstractButton) = {
    button.setPreferredSize((22, 22))
    button.setUI(PaletteButtonUI.createUI(button).asInstanceOf[PaletteButtonUI])
    button.setHideActionText(true)
    button
  }
}

private object ActionToolBar {
  def createButtons(drawingEditor: DrawingEditor) = {
    val canvasColor = ButtonFactory.createDrawingColorButton(drawingEditor, AttributeKeys.CANVAS_FILL_COLOR,
      ButtonFactory.DEFAULT_COLORS, ButtonFactory.DEFAULT_COLORS_COLUMN_COUNT, "attribute.canvasFillColor",
      FormResourceBundleUtil, null, new Rectangle(3, 16, 16, 3))

    for (component <- canvasColor.getPopupMenu.getComponents
         if component.isInstanceOf[JMenuItem] && component.asInstanceOf[JMenuItem].getAction.isInstanceOf[AbstractDrawingViewAction]) {
      component.asInstanceOf[JMenuItem].getAction.asInstanceOf[AbstractDrawingViewAction].setUpdateEnabledState(true)
    }
    new DrawingComponentRepainter(drawingEditor, canvasColor)

    val gridSize = new TextField {
      tooltip = LocaleManager.getString("gridSize")
      reactions += {
        case ValueChanged(_) => {
          try {
            drawingEditor.getActiveView.getConstrainer.asInstanceOf[GridConstrainer].setHeight(text.toInt)
            drawingEditor.getActiveView.getConstrainer.asInstanceOf[GridConstrainer].setWidth(text.toInt)
            drawingEditor.getActiveView.getComponent.repaint()
          } catch {
            case e: NumberFormatException =>
          }
        }
      }
    }

    drawingEditor.addPropertyChangeListener(new PropertyChangeListener {
      def propertyChange(evt: PropertyChangeEvent) = evt.getPropertyName match {
        case DrawingEditor.ACTIVE_VIEW_PROPERTY =>
          gridSize.text = drawingEditor.getActiveView.getConstrainer.asInstanceOf[GridConstrainer].getWidth.toInt.toString
        case _ =>
      }
    })


    List[(String, Any)](
      (null, FormContextManager.undoRedoManager.getUndoAction),
      (null, FormContextManager.undoRedoManager.getRedoAction),
      (null, new ZoomInAction(drawingEditor)),
      (null, new ZoomOutAction(drawingEditor)),
      (null, new ToggleGridAction(drawingEditor)),
      (null, gridSize)
//      (null, canvasColor)
    )
  }
}
