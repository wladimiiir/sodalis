/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.event

import sk.magiksoft.sodalis.psyche.rorschach.entity.BlotAnswer
import scala.swing.event.Event

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/19/11
 * Time: 12:21 PM
 * To change this template use File | Settings | File Templates.
 */

case class BlotAnswerChanged(answer: Option[BlotAnswer]) extends Event