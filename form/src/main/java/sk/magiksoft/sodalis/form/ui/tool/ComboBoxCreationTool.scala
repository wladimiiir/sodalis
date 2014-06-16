
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.form.ui.tool

import sk.magiksoft.sodalis.form.ui.figure.{ItemsHolderFigure, FloatingComboBox}
import java.awt.geom.Point2D
import java.awt.event.{ActionEvent, MouseEvent, ActionListener}

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Apr 28, 2010
 * Time: 3:14:24 PM
 * To change this template use File | Settings | File Templates.
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
    var v: DrawingView = getView
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