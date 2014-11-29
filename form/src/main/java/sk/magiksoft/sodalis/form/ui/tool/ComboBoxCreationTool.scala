package sk.magiksoft.sodalis.form.ui.tool

import sk.magiksoft.sodalis.form.ui.figure.{ItemsHolderFigure, FloatingComboBox}
import java.awt.geom.Point2D
import java.awt.event.{ActionEvent, MouseEvent, ActionListener}
import org.jhotdraw.draw.{Figure, DrawingView, DrawingEditor, AttributeKey}
import org.jhotdraw.draw.tool.CreationTool

/**
 * @author wladimiiir
 * @since 2010/4/28
 */

class ComboBoxCreationTool(figure: ItemsHolderFigure, attributes: java.util.Map[AttributeKey[_], Object]) extends CreationTool(figure, attributes) with ActionListener {
  var lastFigure: ItemsHolderFigure = null
  var comboBox: FloatingComboBox = new FloatingComboBox {
    addActionListener(ComboBoxCreationTool.this)
  }
  var adjusting = false

  def this(figure: ItemsHolderFigure) {
    this(figure, null)
  }

  override def deactivate(editor: DrawingEditor) = {
    endEdit
    super.deactivate(editor)
  }

  /**
   * Creates a new figure at the location where the mouse was pressed.
   */
  override def mousePressed(e: MouseEvent): Unit = {
    var itemsHolderFigure: ItemsHolderFigure = null
    val v: DrawingView = getView
    var p: Point2D.Double = v.viewToDrawing(e.getPoint)
    var pressedFigure: Figure = null

    if (lastFigure != null) {
      endEdit
      if (isToolDoneAfterCreation) {
        fireToolDone
      }
    } else {
      super.mousePressed(e)
      itemsHolderFigure = getCreatedFigure.asInstanceOf[ItemsHolderFigure]
      getView.clearSelection
      getView.addToSelection(itemsHolderFigure)
      beginEdit(itemsHolderFigure)
    }
  }


  override def mouseReleased(evt: MouseEvent) = {}

  def actionPerformed(e: ActionEvent) = {
    if (!adjusting && lastFigure != null) {
      lastFigure.willChange
      lastFigure.selectedItem = comboBox.selectedItem
      lastFigure.setText(lastFigure.selectedItem)
      println(lastFigure.selectedItem)
      lastFigure.changed
      endEdit
      if (isToolDoneAfterCreation) {
        fireToolDone
      }
    }
  }

  def beginEdit(figure: ItemsHolderFigure) = {
    adjusting = true
    lastFigure = figure
    comboBox.createOverlay(getView, figure)
    adjusting = false
  }

  def endEdit = {
    comboBox.endOverlay
    lastFigure = null;
  }
}
