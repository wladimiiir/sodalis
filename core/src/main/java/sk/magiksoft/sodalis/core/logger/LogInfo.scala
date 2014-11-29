package sk.magiksoft.sodalis.core.logger

import org.dom4j.Element

/**
 * @author wladimiiir
 * @since 2011/2/4
 */
trait LogInfo {
  def addLogInfoNode(parent: Element): Unit
}
