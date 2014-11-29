package sk.magiksoft.sodalis.form.ui

import action.{PageEdited, PageRemoved, PageAdded}
import org.jhotdraw.draw._

import swing.GridBagPanel.{Anchor, Fill}
import javax.swing.BorderFactory
import org.jhotdraw.undo.UndoRedoManager
import java.beans.{PropertyChangeEvent, PropertyChangeListener}
import scala.swing.Swing._
import scala.swing._
import java.awt.{Graphics2D, Color, Font, Insets}
import collection.mutable.ListBuffer
import javax.swing.event.{UndoableEditEvent, UndoableEditListener}
import scala.swing.event.ButtonClicked
import sk.magiksoft.sodalis.core.utils.Conversions._

/**
 * @author wladimiiir
 * @since 2010/5/3
 */

class DocumentView(drawingEditor: DrawingEditor, undoRedoManager: UndoRedoManager) extends scala.swing.GridBagPanel {
  private val attributes: Map[AttributeKey[Any], Any] = Map()
  private val drawingViews = new ListBuffer[DrawingView]
  var documentName: String = ""
  var canvasSize = (595, 842)

  background = new Color(152, 152, 120)

  initListeners

  def initListeners = {
    drawingEditor.addPropertyChangeListener(new PropertyChangeListener {
      def propertyChange(evt: PropertyChangeEvent) = {
        evt.getPropertyName match {
          case DrawingEditor.ACTIVE_VIEW_PROPERTY => {
            for (view <- drawingViews) {
              view.getComponent.repaint()
            }
          }
          case _ =>
        }
      }
    })
  }

  def addPage(drawing: Drawing) = insertPage(drawing, drawingViews.size)

  def setDrawings(drawings: List[Drawing]) = {
    drawingViews.clear
    peer.removeAll
    for (drawing <- drawings) {
      addPage(drawing)
    }
  }

  def getDrawings: List[Drawing] = drawingViews.map(dv => dv.getDrawing).toList

  def insertPage(index: Int): Unit = insertPage(createDrawing, index)

  def createDrawing = new DefaultDrawing {
    set(AttributeKeys.CANVAS_WIDTH, new java.lang.Double(canvasSize._1))
    set(AttributeKeys.CANVAS_HEIGHT, new java.lang.Double(canvasSize._2))
  }

  def insertPage(drawing: Drawing, index: Int) = {
    val drawingView = new FormDrawingView

    if (undoRedoManager != null) {
      drawing.addUndoableEditListener(undoRedoManager)
    }
    drawing.addUndoableEditListener(new UndoableEditListener {
      def undoableEditHappened(e: UndoableEditEvent) = publish(new PageEdited(drawingView))
    })
    drawingView.setDrawing(drawing)

    add(drawingView.getComponent, new Constraints {
      gridx = 0
      fill = Fill.Both
      insets = new Insets(25, 10, 0, 10)
    }, index * 2)
    add(new AddRemovePagePanel(drawingView), new Constraints {
      gridx = 0
      anchor = scala.swing.GridBagPanel.Anchor.East
      insets = new Insets(0, 10, 25, 10)
    }, index * 2 + 1)

    drawingViews.insert(index, drawingView)
    drawingEditor.add(drawingView)
    drawingEditor.setActiveView(drawingView)

    revalidate
    repaint
    publish(new PageAdded(drawingView))
  }

  protected def add(c: Component, l: Constraints, index: Int) {
    peer.add(c.peer, l.peer, index)
  }

  def removePage(index: Int) = {
    if (drawingViews.size > 1) {
      _contents --= contents.slice(index * 2, index * 2 + 2)
      val view: DrawingView = drawingViews.remove(index)
      drawingEditor.remove(view)
      revalidate
      repaint
      publish(new PageRemoved(view))
    }
  }

  private class AddRemovePagePanel(drawingView: DrawingView) extends GridBagPanel {
    val addButton = new Button {
      border = BorderFactory.createMatteBorder(1, 1, 1, 0, Color.DARK_GRAY)
      font = font.deriveFont(Font.BOLD, 10f)
      text = "+"
      focusPainted = false
      opaque = false
      preferredSize = (17, 17)
    }
    val removeButton = new Button {
      border = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY)
      font = font.deriveFont(Font.BOLD, 10f)
      text = "-"
      focusPainted = false
      opaque = false
      preferredSize = (17, 17)
    }
    opaque = false

    add(new Panel {
      preferredSize = (30, 25)

      override protected def paintComponent(g: Graphics2D) = g.drawString(drawingViews.indexOf(drawingView) + 1 + "/" + drawingViews.size, 0, 17)
    }, new Constraints {
      gridy = 0
      gridx = 0
      weightx = 1.0
      anchor = Anchor.West
      fill = GridBagPanel.Fill.Both
    })
    add(addButton, new Constraints {
      gridx = 1
      gridy = 0
      weightx = 0.0
    })
    add(removeButton, new Constraints {
      gridx = 2
      gridy = 0
      weightx = 0.0
    })

    listenTo(addButton, removeButton)
    reactions += {
      case ButtonClicked(`addButton`) => insertPage(drawingViews.indexOf(drawingView) + 1)
      case ButtonClicked(`removeButton`) => removePage(drawingViews.indexOf(drawingView))
    }
  }

}
