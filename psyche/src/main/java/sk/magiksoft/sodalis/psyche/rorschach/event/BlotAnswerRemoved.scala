package sk.magiksoft.sodalis.psyche.rorschach.event

/*
 * Copyright (c) 2011
 */

import sk.magiksoft.sodalis.psyche.rorschach.entity.BlotAnswer
import scala.swing.event.Event

/**
 * @author wladimiiir
 * @since 2011/5/22
 */

case class BlotAnswerRemoved(answer: BlotAnswer) extends Event
