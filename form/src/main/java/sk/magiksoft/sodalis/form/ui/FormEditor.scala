
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.form.ui

import sk.magiksoft.sodalis.form.entity._
import sk.magiksoft.sodalis.core.entity.Entity
import sk.magiksoft.sodalis.core.factory.ColorList
import swing._
import org.jhotdraw.gui.JDisclosureToolBar
import java.awt._
import toolbar.{ActionToolBar, AttributesToolBar, ToolsToolBar}
import sk.magiksoft.sodalis.form.FormContextManager

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Apr 13, 2010
 * Time: 8:59:42 PM
 * To change this template use File | Settings | File Templates.
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
    val drawings = getFormDrawings.map(fd => fd.clone.asInstanceOf[FormDrawing])
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