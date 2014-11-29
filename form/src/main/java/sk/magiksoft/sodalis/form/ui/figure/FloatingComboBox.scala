package sk.magiksoft.sodalis.form.ui.figure

import java.awt._
import event.ActionListener
import geom.{Point2D, Rectangle2D}
import org.jhotdraw.draw.event.{FigureEvent, FigureListener, FigureAdapter}
import org.jhotdraw.draw.{DrawingView, AttributeKeys}
import javax.swing.JComboBox
import java.lang.Math._
import sk.magiksoft.sodalis.core.utils.Conversions._

/**
 * @author wladimiiir
 * @since 2010/4/28
 */

class FloatingComboBox {
  private var editedFigure: ItemsHolderFigure = null
  private val comboBox = new JComboBox[String]
  private var view: DrawingView = null
  private val figureHandler: FigureListener = new FigureAdapter {
    override def attributeChanged(e: FigureEvent): Unit = {
      updateWidget
    }
  }

  def createOverlay(view: DrawingView, figure: ItemsHolderFigure): Unit = {
    editedFigure = figure
    editedFigure.addFigureListener(figureHandler)
    this.view = view
    view.getComponent.add(comboBox, 0)
    comboBox.setVisible(true)
    updateWidget
    comboBox.showPopup
  }

  protected def updateWidget: Unit = {
    val font: Font = editedFigure.getFont.deriveFont(editedFigure.getFont.getStyle, (editedFigure.getFontSize * view.getScaleFactor).asInstanceOf[Float])
    val drawBounds: Rectangle2D.Double = editedFigure.getBounds
    val drawLocation: Point2D.Double = new Point2D.Double(drawBounds.x, drawBounds.y)

    if (editedFigure.get(AttributeKeys.TRANSFORM) != null) {
      editedFigure.get(AttributeKeys.TRANSFORM).transform(drawLocation, drawLocation)
    }
    val viewBounds: Rectangle = view.drawingToView(drawBounds)
    val viewLocation: Point = view.drawingToView(drawLocation)
    val fontBaseline: Float = comboBox.getGraphics.getFontMetrics(font).getMaxAscent
    val fBaseline: Double = editedFigure.getBaseline * view.getScaleFactor
    var maxItem = """"""
    val figure: ItemsHolderFigure = editedFigure;

    viewBounds.x = viewLocation.x
    viewBounds.y = viewLocation.y

    comboBox.removeAllItems
    for (item <- figure.items) {
      comboBox.addItem(item)
      if (item.length > maxItem.length) {
        maxItem = item
      }
    }
    comboBox.setSelectedItem(figure.selectedItem)
    comboBox.setFont(font)
    comboBox.setPrototypeDisplayValue(maxItem)
    val size = comboBox.getPreferredSize
    comboBox.setForeground(figure.getTextColor)
    comboBox.setBackground(figure.getFillColor)
    comboBox.setBounds(viewBounds.x, viewBounds.y - (fontBaseline - fBaseline), max(viewBounds.width, size.width), max(viewBounds.height, size.height))
  }

  def selectedItem = comboBox.getSelectedItem match {
    case item: String => item
    case _ => null
  }

  def endOverlay: Unit = {
    view.getComponent.requestFocus

    if (comboBox != null) {
      comboBox.setVisible(false)
      view.getComponent.remove(comboBox)

      val bounds: Rectangle = comboBox.getBounds
      view.getComponent.repaint(bounds.x, bounds.y, bounds.width, bounds.height)
    }
    if (editedFigure != null) {
      editedFigure.removeFigureListener(figureHandler)
      editedFigure = null
    }
  }

  def addActionListener(listener: ActionListener) = comboBox.addActionListener(listener)
}
