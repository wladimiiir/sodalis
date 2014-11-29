package sk.magiksoft.sodalis.psyche.rorschach.event

import sk.magiksoft.sodalis.psyche.rorschach.entity.SigningMethod
import scala.swing.event.Event

/**
 * @author wladimiiir
 * @since 2011/5/22
 */

case class SigningMethodChanged(method: SigningMethod.Value) extends Event {

}
