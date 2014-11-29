package sk.magiksoft.sodalis.psyche.rorschach.event

/*
 * Copyright (c) 2011
 */

import sk.magiksoft.sodalis.psyche.rorschach.entity.TestResult
import scala.swing.event.Event

/**
 * @author wladimiiir
 * @since 2011/5/17
 */

case class TestResultChanged(result: TestResult) extends Event
