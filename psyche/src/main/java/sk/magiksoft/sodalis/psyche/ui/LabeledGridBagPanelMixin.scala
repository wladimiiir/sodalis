package sk.magiksoft.sodalis.psyche.ui

import scala.swing._
import java.awt.{Insets, Font}
import swing.GridBagPanel._
import Swing._

/**
 * @author wladimiiir
 * @since 2011/6/9
 */

trait LabeledGridBagPanelMixin extends GridBagPanel {
  protected val c = new Constraints()

  protected def createLabel = new Label() {
    preferredSize = (300, 19)
    horizontalAlignment = Alignment.Left
    font = font.deriveFont(Font.BOLD)
  }

  protected def addLabeledComponent(label: String, component: Component) {
    c.gridx = 0
    c.gridy += 1
    c.anchor = Anchor.East
    c.insets = new Insets(2, 2, 0, 0)
    add(new Label(label), c)
    c.gridx += 1
    c.anchor = Anchor.West
    c.insets = new Insets(2, 5, 0, 2)
    add(component, c)
  }
}
