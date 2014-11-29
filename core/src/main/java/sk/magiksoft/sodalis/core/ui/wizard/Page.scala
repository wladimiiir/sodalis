package sk.magiksoft.sodalis.core.ui.wizard

import swing.Component

/**
 * @author wladimiiir
 * @since 2011/2/18
 */
trait Page {
  def getPreviousPage: Option[Page]

  def getNextPage: Option[Page]

  def getComponent: Component
}
