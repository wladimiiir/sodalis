package sk.magiksoft.sodalis.psyche.rorschach.event

import sk.magiksoft.sodalis.psyche.rorschach.entity.BlotAnswer
import scala.swing.event.Event

/**
 * @author wladimiiir
 * @since 2011/5/22
 */

case class BlotAnswerAdded(answer: BlotAnswer) extends Event
