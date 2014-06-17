/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.ui.interpretation

/*
 * Copyright (c) 2011
 */

import sk.magiksoft.sodalis.psyche.rorschach.entity.TestResult
import scala.swing.{Swing, ScrollPane, TextArea, GridBagPanel}
import javax.swing.BorderFactory
import Swing._

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/22/11
 * Time: 7:55 AM
 * To change this template use File | Settings | File Templates.
 */

class SpecialSignsInterpretationPanel extends GridBagPanel with InterpretationPanel {
  private val specialSigns = new TextArea {
    preferredSize = (500, 60)
    wordWrap = true
    lineWrap = true
    editable = false
  }

  add(new ScrollPane(specialSigns) {
    border = BorderFactory.createEmptyBorder()
  }, new Constraints {
    grid = (0, 0)
  })

  protected def setupValues(testResult: Option[TestResult]) {
    testResult match {
      case Some(result) => {
        specialSigns.text = result.findAnswers(answer => answer.specialSigns.toList).groupBy(sign => sign.name).map {
          tuple => (if (tuple._2.size == 1) "" else (tuple._2.size.toString() + "x")) + tuple._1
        }.mkString(", ")
      }
      case None => {
        specialSigns.text = ""
      }
    }
  }
}