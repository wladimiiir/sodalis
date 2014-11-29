package sk.magiksoft.sodalis.psyche.rorschach.event

import sk.magiksoft.sodalis.psyche.rorschach.entity.BlotSigning
import scala.swing.event.Event

/**
 * @author wladimiiir
 * @since 2011/5/19
 */

case class BlotSigningChanged(signing: BlotSigning) extends Event
