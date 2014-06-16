/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.ui.interpretation

import swing.UIElement._
import scala.swing.Swing._
import sk.magiksoft.sodalis.psyche.rorschach.entity.TestResult
import sk.magiksoft.sodalis.psyche.rorschach.event.TableAnswerAdded._
import sk.magiksoft.sodalis.psyche.rorschach.event.TableAnswerEdited._
import sk.magiksoft.sodalis.psyche.rorschach.event.{TestResultChanged, TableAnswerAdded, TableAnswerRemoved, TableAnswerEdited}
import swing.{Reactor, Alignment, Label}
import java.awt.Font

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/22/11
 * Time: 7:55 AM
 * To change this template use File | Settings | File Templates.
 */

trait InterpretationPanel extends Reactor {

  private var testResult: Option[TestResult] = None

  reactions += {
    case TestResultChanged(result) => {
      testResult = Option(result)
      setupValues(testResult)
    }
    case TableAnswerAdded(_) => setupValues(testResult)
    case TableAnswerRemoved(_) => setupValues(testResult)
    case TableAnswerEdited(_) => setupValues(testResult)
  }

  protected def setupValues(testResult: Option[TestResult])
}