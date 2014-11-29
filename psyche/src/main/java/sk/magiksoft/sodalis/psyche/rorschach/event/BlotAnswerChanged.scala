package sk.magiksoft.sodalis.psyche.rorschach.event

import sk.magiksoft.sodalis.psyche.rorschach.entity.BlotAnswer
import scala.swing.event.Event

/**
 * @author wladimiiir
 * @since 2011/5/19
 */

case class BlotAnswerChanged(answer: Option[BlotAnswer]) extends Event
