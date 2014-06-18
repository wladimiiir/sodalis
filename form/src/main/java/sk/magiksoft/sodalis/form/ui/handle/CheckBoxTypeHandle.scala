
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.form.ui.handle

import sk.magiksoft.sodalis.form.ui.figure.CheckBoxFigure
import java.awt._
import sk.magiksoft.sodalis.form.ui.figure.CheckType
import org.jhotdraw.draw.handle.AbstractHandle
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.utils.Conversions._

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Apr 23, 2010
 * Time: 7:55:44 PM
 * To change this template use File | Settings | File Templates.
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