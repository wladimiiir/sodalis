
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.swing

import swing.{Component, Panel, LayoutContainer}

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jun 22, 2010
 * Time: 6:00:00 PM
 * To change this template use File | Settings | File Templates.
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