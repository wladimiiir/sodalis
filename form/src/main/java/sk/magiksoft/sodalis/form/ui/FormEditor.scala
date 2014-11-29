package sk.magiksoft.sodalis.form.ui

import sk.magiksoft.sodalis.form.entity._
import sk.magiksoft.sodalis.core.factory.ColorList
import swing._
import toolbar.{ActionToolBar, AttributesToolBar, ToolsToolBar}
import sk.magiksoft.sodalis.form.FormContextManager
import org.jhotdraw.draw.DefaultDrawingEditor
import scala.collection.mutable.ListBuffer
import scala.swing.ScrollPane.BarPolicy
import sk.magiksoft.sodalis.core.utils.{Conversions, UIUtils}
import Conversions._
import org.jhotdraw.gui.JDisclosureToolBar

/**
 * @author wladimiiir
 * @since 2010/4/13
 */

class FormEditor extends BorderPanel {
  val drawingEditor = new DefaultDrawingEditor
  val documentView = new DocumentView(drawingEditor, FormContextManager.undoRedoManager) {
    override def createDrawing = new FormDrawing(canvasSize._1, canvasSize._2)
  }
  private val toolBars: List[JDisclosureToolBar] = List(
    new ActionToolBar(drawingEditor),
    new ToolsToolBar(drawingEditor),
    new AttributesToolBar(drawingEditor)
  )

  initComponents()
  listenTo(documentView)

  def setFormDrawings(drawings: List[FormDrawing]) = documentView.setDrawings(drawings)

  def getFormDrawings: List[FormDrawing] = documentView.getDrawings.filter(d => d.isInstanceOf[FormDrawing]).asInstanceOf[List[FormDrawing]]

  def setupForm(form: Form) = {
    val drawings = getFormDrawings.map(_.clone.asInstanceOf[FormDrawing])
    for (drawing <- drawings) {
      drawing.clear
    }
    form.pages = new ListBuffer[FormDrawing] ++ drawings
  }

  private def initComponents() = {
    add(createToolBarPanel, BorderPanel.Position.North)
    add(new ScrollPane(documentView) {
      verticalScrollBarPolicy = BarPolicy.Always
      verticalScrollBar.unitIncrement = 25
    }, BorderPanel.Position.Center)

    def createToolBarPanel = UIUtils.makeGradientComponent(new FlowPanel(FlowPanel.Alignment.Leading)() {
      for (toolBar <- toolBars) {
        contents += toolBar
        toolBar.setDisclosureState(1)
      }
    }, ColorList.LIGHT_BLUE, ColorList.SPLASH_FOREGROUND)
  }
}
