
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.form.ui.tool

import sk.magiksoft.sodalis.form.ui.figure.{ItemsHolderFigure, FloatingComboBox}
import java.awt.event.{ActionEvent, ActionListener, MouseEvent}
import org.jhotdraw.draw.DrawingEditor
import org.jhotdraw.draw.tool.AbstractTool

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Apr 28, 2010
 * Time: 10:05:17 AM
 * To change this template use File | Settings | File Templates.
 */

class ComboBoxEditingTool(itemsHolderFigure: ItemsHolderFigure) extends AbstractTool with ActionListener {
  var comboBox = new FloatingComboBox {
    addActionListener(ComboBoxEditingTool.this)
  }
  var adjusting = false

  def mouseDragged(e: MouseEvent) = {}

  override def mousePressed(evt: MouseEvent) = {
    adjusting = true
    comboBox.createOverlay(getView, itemsHolderFigure)
    adjusting = false
  }

  override def deactivate(editor: DrawingEditor) = {
    comboBox.endOverlay
  }


  def actionPerformed(e: ActionEvent) = {
    if (!adjusting) {
      itemsHolderFigure.willChange
      itemsHolderFigure.selectedItem = comboBox.selectedItem
      itemsHolderFigure.setText(itemsHolderFigure.selectedItem)
      itemsHolderFigure.changed
      comboBox.endOverlay
      fireToolDone
    }
  }
}