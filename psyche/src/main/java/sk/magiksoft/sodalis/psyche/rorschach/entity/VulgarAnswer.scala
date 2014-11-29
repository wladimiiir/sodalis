package sk.magiksoft.sodalis.psyche.rorschach.entity

import scala.beans.BeanProperty


/**
 * @author wladimiiir
 * @since 2011/5/15
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

