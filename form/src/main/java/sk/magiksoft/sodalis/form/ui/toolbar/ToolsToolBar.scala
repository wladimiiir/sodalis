package sk.magiksoft.sodalis.form.ui.toolbar

import sk.magiksoft.sodalis.form.locale.FormResourceBundleUtil
import java.lang.String
import javax.swing.JToggleButton
import sk.magiksoft.sodalis.form.ui.figure.ComboBoxFigure
import sk.magiksoft.sodalis.form.ui.tool.{ComboBoxCreationTool, FormEditTool}
import sk.magiksoft.sodalis.form.ui.figure.CheckBoxFigure
import org.jhotdraw.draw._
import org.jhotdraw.gui.plaf.palette.PaletteButtonUI
import org.jhotdraw.draw.tool._
import org.jhotdraw.draw.action.ButtonFactory
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.utils.Conversions._
import scala.swing.Swing._

/**
 * @author wladimiiir
 * @since 2010/4/19
 */

class ToolsToolBar(editor: DrawingEditor, tools: List[(String, Tool)]) extends AbstractToolBar(LocaleManager.getString("tools"), editor, tools) {
  def this(drawingEditor: DrawingEditor) = this(drawingEditor, ToolsToolBar.createButtonItems(drawingEditor))

  protected def createComponent(item: (String, Any)) = {
    item._2 match {
      case selectionTool: DelegationSelectionTool => setupButton(ButtonFactory.addSelectionToolTo(ToolsToolBar.this, editor, selectionTool))
      case tool: Tool => setupButton(ButtonFactory.addToolTo(ToolsToolBar.this, editor, tool, item._1, FormResourceBundleUtil))
    }
  }

  protected def setupButton(button: JToggleButton) = {
    button.setPreferredSize((22, 22))
    button.setUI(PaletteButtonUI.createUI(button).asInstanceOf[PaletteButtonUI])
    button
  }
}

private object ToolsToolBar {
  def createButtonItems(drawingEditor: DrawingEditor) = List(
    ("selectionTool", new DelegationSelectionTool(ButtonFactory.createDrawingActions(drawingEditor), ButtonFactory.createSelectionActions(drawingEditor))),
    ("editTool", new FormEditTool),
    ("edit.createLine", new CreationTool(new LineFigure)),
    ("edit.createRectangle", new CreationTool(new RoundRectangleFigure {
      roundrect.arcwidth = 0
      roundrect.archeight = 0
    })),
    ("edit.createEllipse", new CreationTool(new EllipseFigure)),
    ("edit.createScribble", new BezierTool(new BezierFigure(false)) {
      setToolDoneAfterCreation(true)
    }),
    ("edit.createPolygon", new BezierTool(new BezierFigure(true)) {
      setToolDoneAfterCreation(true)
    }),
    ("edit.createTextArea", new TextAreaCreationTool(new TextAreaFigure)),
    ("edit.createCheckBox", new CreationTool(new CheckBoxFigure {
      roundrect.width = 12
      roundrect.height = 12
      roundrect.arcwidth = 0
      roundrect.archeight = 0
    })),
    ("edit.createComboBox", new ComboBoxCreationTool(new ComboBoxFigure)),
    ("edit.createImage", new ImageTool(new ImageFigure)),
    ("edit.createLineConnection", new ConnectionTool(new LineConnectionFigure))
  )
}
