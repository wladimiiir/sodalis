package sk.magiksoft.sodalis.item.event

import sk.magiksoft.sodalis.item.entity.Item
import scala.swing.event.Event

/**
 * @author wladimiiir
 * @since 2010/6/25
 */

case class ItemSelectionChanged(item: Option[Item]) extends Event
