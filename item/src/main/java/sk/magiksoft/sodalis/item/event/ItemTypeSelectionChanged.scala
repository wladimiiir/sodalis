package sk.magiksoft.sodalis.item.event

import sk.magiksoft.sodalis.item.entity.ItemType
import scala.swing.event.Event

/**
 * @author wladimiiir
 * @since 2010/6/25
 */

case class ItemTypeSelectionChanged(itemType: Option[ItemType]) extends Event
