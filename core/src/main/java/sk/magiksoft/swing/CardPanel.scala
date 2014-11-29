package sk.magiksoft.swing

import swing.{Component, Panel, LayoutContainer}

/**
 * @author wladimiiir
 * @since 2010/6/22
 */

class CardPanel extends Panel with LayoutContainer {

  import java.awt.CardLayout

  var currentConstraints: Option[Constraints] = None

  def layoutManager = peer.getLayout.asInstanceOf[CardLayout]

  override lazy val peer = new javax.swing.JPanel(new CardLayout) with SuperMixin

  type Constraints = String

  def show(con: String) = {
    layoutManager.show(peer, con)
    currentConstraints = Option(con)
  }

  protected def constraintsFor(comp: Component) = null

  protected def areValid(c: Constraints): (Boolean, String) = (true, "")

  protected def add(c: Component, l: Constraints) {
    peer.add(c.peer, l)
  }


}
