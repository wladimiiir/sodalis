/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.event

/*
 * Copyright (c) 2011
 */

import sk.magiksoft.sodalis.psyche.rorschach.entity.TestResult
import scala.swing.event.Event

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/17/11
 * Time: 6:57 PM
 * To change this template use File | Settings | File Templates.
 */

case class TestResultChanged(result: TestResult) extends Event