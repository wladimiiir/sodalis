package sk.magiksoft.sodalis.form.ui.tool

import scala.swing.event.MouseEvent


/**
 * @author wladimiiir
 * @since 2010/4/23
 */

trait MouseHandler {
  def handleMousePressed(e: MouseEvent)

  def handleMouseClicked(e: MouseEvent)
}
