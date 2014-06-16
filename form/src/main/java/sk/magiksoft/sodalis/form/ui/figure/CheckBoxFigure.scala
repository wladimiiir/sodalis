
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.form.ui.figure

import sk.magiksoft.sodalis.form.ui.figure.CheckType._
import sk.magiksoft.sodalis.form.ui.handle.CheckBoxTypeHandle
import java.awt.Graphics2D
import java.awt.geom.RoundRectangle2D.Double
import java.awt.geom.Point2D
import sk.magiksoft.sodalis.form.ui.tool.{MouseHandlerTool, MouseHandler}

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Apr 23, 2010
 * Time: 1:01:29 PM
 * To change this template use File | Settings | File Templates.
 */

class CheckBoxFigure extends RoundRectangleFigure with MouseHandler {
  var selected = true
  var checkType = Check

  override def drawStroke(g: Graphics2D) = {
    var r: Double = roundrect.clone.asInstanceOf[Double]
    var grow = AttributeKeys.getPerpendicularDrawGrowth(this)

    r.x -= grow
    r.y -= grow
    r.width += grow * 2
    r.height += grow * 2
    r.arcwidth += grow * 2
    r.archeight += grow * 2

    if (r.width > 0 && r.height > 0) {
      g.draw(r)
      if (selected) {
        drawCheck(g, r)
      }
    }
  }

  override def getTool(p: Point2D.Double): Tool = {
    if (contains(p)) {
      return new MouseHandlerTool(this)
    }
    return null;
  }

  def drawCheck(g: Graphics2D, r: Double) = {
    val arcOffset = math.max(0, math.min(r.arcwidth / 4, r.archeight / 4))

    checkType match {
      case Cross => {
        g.drawLine(r.x + arcOffset, r.y + arcOffset, r.x + r.width - arcOffset, r.y + r.height - arcOffset)
        g.drawLine(r.x + arcOffset, r.y + r.height - arcOffset, r.x + r.width - arcOffset, r.y + arcOffset)
      }
      case Check => {
        g.drawLine(r.x + r.width / 5, r.y + r.height / 2, r.x + r.width / 2, r.y + r.height * 4 / 5)
        g.drawLine(r.x + r.width / 2, r.y + r.height * 4 / 5, r.x + r.width * 4 / 5, r.y + r.height / 5)
      }
      case CrossLine => {
        g.drawLine(r.x + arcOffset, r.y + r.height - arcOffset, r.x + r.width - arcOffset, r.y + arcOffset)
      }
      case Strike => {
        g.drawLine(r.x, r.y + r.height / 2, r.x + r.width, r.y + r.height / 2)
      }
    }
  }

  override def createHandles(detailLevel: Int) = {
    var handles = super.createHandles(detailLevel)

    handles.add(new CheckBoxTypeHandle(this))

    handles
  }


  def handleMousePressed(e: MouseEvent) = {
    selected = !selected

    if (selected) {
      for (groupFigure <- findGroupFigures) {
        for (figure <- groupFigure.getChildren()) {
          figure match {
            case checkBoxFigure: CheckBoxFigure => if (checkBoxFigure != this) checkBoxFigure.selected = false
            case _ =>
          }
        }
      }
    }

    fireFigureChanged
  }

  private def findGroupFigures: Buffer[GroupFigure] = {
    var figures: Buffer[Figure] = getDrawing.getChildren

    figures.filter((f: Figure) => f match {
      case gf: GroupFigure => gf.contains(CheckBoxFigure.this)
      case _ => false
    }).asInstanceOf[Buffer[GroupFigure]]
  }

  def handleMouseClicked(e: MouseEvent) = {}
}