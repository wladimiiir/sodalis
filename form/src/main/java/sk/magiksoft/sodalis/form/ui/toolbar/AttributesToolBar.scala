package sk.magiksoft.sodalis.form.ui.toolbar

import java.lang.String
import sk.magiksoft.sodalis.form.locale.FormResourceBundleUtil
import javax.swing._
import swing.Swing._
import org.jhotdraw.draw.{DrawingEditor, AttributeKeys}
import java.awt.Rectangle
import org.jhotdraw.gui.plaf.palette.PaletteButtonUI
import org.jhotdraw.draw.action._
import sk.magiksoft.sodalis.core.locale.LocaleManager
import scala.swing.Button
import sk.magiksoft.sodalis.core.utils.Conversions._

/**
 * @author wladimiiir
 * @since 2010/5/8
 */

class AttributesToolBar(drawingEditor: DrawingEditor, buttonItems: List[(String, Any)]) extends AbstractToolBar(LocaleManager.getString("attributes"), drawingEditor, buttonItems) {
  def this(drawingEditor: DrawingEditor) = this(drawingEditor, AttributesToolBar.createButtonItems(drawingEditor))

  protected def createComponent(item: (String, Any)) = {
    item._2 match {
      case action: Action => {
        val button = new Button(action) {
          text = null
          preferredSize = (22, 22)
        }
        button.peer.setUI(PaletteButtonUI.createUI(button).asInstanceOf[PaletteButtonUI])
        button
      }
      case button: AbstractButton => setupButton(button)
      case c: JComponent => c
      case _ => new JToolBar.Separator
    }
  }

  protected def setupButton(button: AbstractButton) = {
    button.setPreferredSize((22, 22))
    button.setUI(PaletteButtonUI.createUI(button).asInstanceOf[PaletteButtonUI])
    button
  }
}

private object AttributesToolBar {
  def createButtonItems(drawingEditor: DrawingEditor) = List(
  (null, new PickAttributesAction(drawingEditor)),
  (null, new ApplyAttributesAction(drawingEditor)), {
    val button = ButtonFactory.createEditorColorButton(drawingEditor, AttributeKeys.STROKE_COLOR, ButtonFactory.DEFAULT_COLORS, ButtonFactory.DEFAULT_COLORS_COLUMN_COUNT, "attribute.strokeColor", FormResourceBundleUtil, null, new Rectangle(3, 16, 16, 3))
    button.setAction(button.getAction, null)
    (null, button)
  }, {
    val button = ButtonFactory.createEditorColorButton(drawingEditor, AttributeKeys.FILL_COLOR, ButtonFactory.DEFAULT_COLORS, ButtonFactory.DEFAULT_COLORS_COLUMN_COUNT, "attribute.fillColor", FormResourceBundleUtil, null, new Rectangle(3, 16, 16, 3))
    button.setAction(button.getAction, null)
    (null, button)
  }, {
    val button = ButtonFactory.createEditorColorButton(drawingEditor, AttributeKeys.TEXT_COLOR, ButtonFactory.DEFAULT_COLORS, ButtonFactory.DEFAULT_COLORS_COLUMN_COUNT, "attribute.textColor", FormResourceBundleUtil, null, new Rectangle(3, 16, 16, 3))
    button.setAction(button.getAction, null)
    (null, button)
  },
  (null, null), (null, null),
  (null, ButtonFactory.createStrokeDecorationButton(drawingEditor)),
  (null, ButtonFactory.createStrokeWidthButton(drawingEditor)),
  (null, ButtonFactory.createStrokeDashesButton(drawingEditor)),
  (null, ButtonFactory.createStrokeTypeButton(drawingEditor)),
  (null, ButtonFactory.createStrokePlacementButton(drawingEditor)),
  (null, ButtonFactory.createStrokeCapButton(drawingEditor)),
  (null, ButtonFactory.createStrokeJoinButton(drawingEditor)),
  (null, ButtonFactory.createFontButton(drawingEditor)),
  (null, ButtonFactory.createFontStyleBoldButton(drawingEditor)),
  (null, ButtonFactory.createFontStyleItalicButton(drawingEditor)),
  (null, ButtonFactory.createFontStyleUnderlineButton(drawingEditor)),
  (null, null), (null, null), (null, null),
  (null, new AlignAction.West(drawingEditor)),
  (null, new AlignAction.North(drawingEditor)),
  (null, new AlignAction.East(drawingEditor)),
  (null, new AlignAction.South(drawingEditor)),
  (null, new BringToFrontAction(drawingEditor)),
  (null, new SendToBackAction(drawingEditor))
  )
}
