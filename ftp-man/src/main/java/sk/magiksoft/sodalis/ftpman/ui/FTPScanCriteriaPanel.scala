/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.ftpman.ui

import java.text.{NumberFormat, Format}
import javax.swing.JLabel
import sk.magiksoft.sodalis.core.locale.LocaleManager
import java.awt.{Button, Insets, GridBagConstraints}
import sk.magiksoft.sodalis.ftpman.entity.FTPScanCriteria
import swing.GridBagPanel.Anchor
import swing.Swing._
import swing._

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/6/11
 * Time: 11:58 AM
 * To change this template use File | Settings | File Templates.
 */

class FTPScanCriteriaPanel extends GridBagPanel {
  private val hostFrom = new TextField{
    preferredSize = (120, 21)
  }
  private val hostTo = new TextField{
    preferredSize = (120, 21)
  }

  initComponents()

  private def initComponents() {

    var x = 0
    var y = 0
    add(new Label(LocaleManager.getString("hostFrom")), new Constraints {
      grid = (x, y)
      insets = new Insets(5, 5, 0, 0)
    })
    x += 1
    add(hostFrom, new Constraints {
      grid = (x, y)
      insets = new Insets(5, 3, 0, 5)
    })
    x = 0
    y += 1
    add(new Label(LocaleManager.getString("hostTo")), new Constraints {
      grid = (x, y)
      insets = new Insets(3, 5, 0, 0)
    })
    x += 1
    add(hostTo, new Constraints {
      grid = (x, y)
      insets = new Insets(3, 3, 0, 5)
    })
  }

  def createCriteria = {
    val criteria = new FTPScanCriteria
    criteria.hostFrom = hostFrom.text
    criteria.hostTo = hostTo.text
    criteria
  }
}