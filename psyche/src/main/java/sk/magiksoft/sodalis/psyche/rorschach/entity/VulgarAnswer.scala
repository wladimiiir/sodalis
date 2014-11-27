/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.entity

import scala.beans.BeanProperty


/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/15/11
 * Time: 7:13 PM
 * To change this template use File | Settings | File Templates.
 */

class VulgarAnswer extends Signing {
  @BeanProperty var blotIndex = 0
  @BeanProperty var localization = ""
  @BeanProperty var percentage = 0
  var vulgarity = Vulgarity.V1

  def getVulgarity = vulgarity.id

  def setVulgarity(vulgarityID: Int) {
    Vulgarity.values.find(_.id == vulgarityID) match {
      case Some(vulgarity) => this.vulgarity = vulgarity
      case None =>
    }
  }
}

