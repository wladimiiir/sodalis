package sk.magiksoft.sodalis.psyche.rorschach.event

import sk.magiksoft.sodalis.psyche.rorschach.entity.RorschachBlot
import scala.swing.event.Event

/**
 * @author wladimiiir
 * @since 2011/5/17
 */

case class RorschachBlotChanged(blot: RorschachBlot) extends Event {

}
