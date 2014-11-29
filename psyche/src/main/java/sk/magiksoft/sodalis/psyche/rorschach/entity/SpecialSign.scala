package sk.magiksoft.sodalis.psyche.rorschach.entity

import scala.beans.BeanProperty


/**
 * @author wladimiiir
 * @since 2011/5/18
 */

class SpecialSign extends Signing {
  @BeanProperty var category = ""
  @BeanProperty var qualitySign = false
}
