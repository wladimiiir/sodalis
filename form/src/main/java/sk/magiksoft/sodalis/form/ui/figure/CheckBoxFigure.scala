package sk.magiksoft.sodalis.form.ui.figure

import sk.magiksoft.sodalis.form.ui.figure.CheckType._
import sk.magiksoft.sodalis.form.ui.handle.CheckBoxTypeHandle
import java.awt.Graphics2D
import java.awt.geom.{RoundRectangle2D, Point2D}
import sk.magiksoft.sodalis.form.ui.tool.{MouseHandlerTool, MouseHandler}
import org.jhotdraw.draw.{AttributeKeys, RoundRectangleFigure, Figure, GroupFigure}
import scala.collection.mutable
import scala.swing.event.MouseEvent
import org.jhotdraw.draw.tool.Tool
import scala.collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2010/4/23
 */

class CheckBoxFigure extends RoundRectangleFigure with MouseHandler {
  var selected = true
  var checkType = Check

  override def drawStroke(g: Graphics2D) = {
    val r: RoundRectangle2D.Double = getBounds.clone.asInstanceOf[RoundRectangle2D.Double]
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

  implicit def double2Int(d: Double): Int = d.toInt

  def drawCheck(g: Graphics2D, r: RoundRectangle2D.Double) = {
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
    val handles = super.createHandles(detailLevel)

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

  private def findGroupFigures: mutable.Buffer[GroupFigure] = {
    val figures: mutable.Buffer[Figure] = getDrawing.getChildren

    figures.filter((f: Figure) => f match {
      case gf: GroupFigure => gf.contains(CheckBoxFigure.this)
      case _ => false
    }).asInstanceOf[mutable.Buffer[GroupFigure]]
  }

  def handleMouseClicked(e: MouseEvent) = {}
}
