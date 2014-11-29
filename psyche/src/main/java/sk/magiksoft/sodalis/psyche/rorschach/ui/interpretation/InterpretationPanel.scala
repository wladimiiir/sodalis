package sk.magiksoft.sodalis.psyche.rorschach.ui.interpretation

import swing.UIElement._
import scala.swing.Swing._
import sk.magiksoft.sodalis.psyche.rorschach.entity.TestResult
import sk.magiksoft.sodalis.psyche.rorschach.event.BlotAnswerAdded._
import sk.magiksoft.sodalis.psyche.rorschach.event.BlotAnswerEdited._
import sk.magiksoft.sodalis.psyche.rorschach.event.{TestResultChanged, BlotAnswerAdded, BlotAnswerRemoved, BlotAnswerEdited}
import swing.{Reactor, Alignment, Label}
import java.awt.Font

/**
 * @author wladimiiir
 * @since 2011/5/22
 */

trait InterpretationPanel extends Reactor {

  private var testResult: Option[TestResult] = None

  reactions += {
    case TestResultChanged(result) => {
      testResult = Option(result)
      setupValues(testResult)
    }
    case BlotAnswerAdded(_) => setupValues(testResult)
    case BlotAnswerRemoved(_) => setupValues(testResult)
    case BlotAnswerEdited(_) => setupValues(testResult)
  }

  protected def setupValues(testResult: Option[TestResult])
}
