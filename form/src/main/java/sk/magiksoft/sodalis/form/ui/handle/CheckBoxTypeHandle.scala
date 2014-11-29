package sk.magiksoft.sodalis.form.ui.handle

import sk.magiksoft.sodalis.form.ui.figure.CheckBoxFigure
import java.awt._
import sk.magiksoft.sodalis.form.ui.figure.CheckType
import org.jhotdraw.draw.handle.AbstractHandle
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.utils.Conversions._

/**
 * @author wladimiiir
 * @since 2010/4/23
 */

class CheckBoxTypeHandle(checkBox: CheckBoxFigure) extends AbstractHandle(checkBox) {

  def basicGetBounds = new Rectangle(checkBox.getBounds.x + checkBox.getBounds.width, checkBox.getBounds.getCenterY + checkBox.getBounds.height / 4, getHandlesize, getHandlesize)

  def trackStart(anchor: Point, modifiersEx: Int) = {}

  def trackStep(anchor: Point, lead: Point, modifiersEx: Int) = {}

  def trackEnd(anchor: Point, lead: Point, modifiersEx: Int) = {
    checkBox.checkType = CheckType((checkBox.checkType.id + 1) % CheckType.values.size)
    checkBox.fireFigureChanged
  }

  override def getCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)

  override def draw(g: Graphics2D) = drawDiamond(g, Color.GREEN, Color.BLACK)

  override def getToolTipText(p: Point) = LocaleManager.getString("checkType")
}
