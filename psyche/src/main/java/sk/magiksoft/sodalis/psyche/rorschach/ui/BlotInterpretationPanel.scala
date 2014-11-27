/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.ui

import interpretation._
import javax.swing.BorderFactory
import java.awt.Color
import sk.magiksoft.sodalis.psyche.rorschach.entity.SigningMethod
import scala.swing.BorderPanel
import sk.magiksoft.sodalis.psyche.rorschach.event.SigningMethodChanged
import sk.magiksoft.swing.CardPanel
import scala.swing.Swing._
import scala.swing.BorderPanel.Position

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/13/11
 * Time: 10:06 AM
 * To change this template use File | Settings | File Templates.
 */

class BlotInterpretationPanel extends BorderPanel {
  initComponents()

  private def initComponents() {
    border = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 0, 0, 5), BorderFactory.createLineBorder(Color.GRAY))
    preferredSize = (100, 250)

    add(new CardPanel {
      for (method <- SigningMethod.valueList) {
        add(createMethodPanel(method), method.id.toString)
      }

      listenTo(BlotInterpretationPanel.this)
      reactions += {
        case SigningMethodChanged(method) => show(method.id.toString)
      }
    }, Position.Center)

    def createMethodPanel(method: SigningMethod.Value) = {
      val panel = method match {
        case SigningMethod.General => new GeneralInterpretationPanel
        case SigningMethod.Aperception => new AperceptionInterpretationPanel
        case SigningMethod.Determinants => new DeterminantsInterpretationPanel
        case SigningMethod.Contents => new ContentsInterpretationPanel
        case SigningMethod.AnswerFrequency => new AnswerFrequencyInterpretationPanel
        case SigningMethod.SpecialSigns => new SpecialSignsInterpretationPanel
      }
      panel.listenTo(this)
      panel
    }
  }
}