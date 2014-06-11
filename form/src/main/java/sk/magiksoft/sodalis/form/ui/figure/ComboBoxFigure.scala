
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.form.ui.figure

import org.jhotdraw.draw.TextFigure
import sk.magiksoft.sodalis.form.ui.tool.ComboBoxEditingTool
import java.awt.font.TextLayout
import java.awt.geom.{Rectangle2D, Point2D}

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Apr 28, 2010
 * Time: 10:03:34 AM
 * To change this template use File | Settings | File Templates.
 */

class ComboBoxFigure extends TextFigure with ItemsHolderFigure {
  setText("")

  override def getTool(p: Point2D.Double) = {
    if (isEditable && contains(p)) {
      new ComboBoxEditingTool(this)
    } else {
      null
    }
  }


  override def getBounds = {
    if (getText.isEmpty) {
      var layout: TextLayout = getTextLayout
      new Rectangle2D.Double(origin.x, origin.y, 50, layout.getAscent + layout.getDescent)
    } else {
      super.getBounds
    }
  }

  override def clone = {
    var cloned = super.clone

    cloned match {
      case cbFigure: ComboBoxFigure => {
        cbFigure.items = this.items
        cbFigure.selectedItem = this.selectedItem
      }
    }

    cloned
  }
}