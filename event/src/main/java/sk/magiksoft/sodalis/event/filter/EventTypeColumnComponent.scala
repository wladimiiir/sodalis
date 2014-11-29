package sk.magiksoft.sodalis.event.filter

import java.lang.StringBuilder
import sk.magiksoft.sodalis.event.entity.EventType
import sk.magiksoft.sodalis.core.filter.element.MultiselectComboboxColumnComponent
import sk.magiksoft.sodalis.core.utils.DatabaseEntityUpdatedList

/**
 * @author wladimiiir
 * @since 2011/2/25
 */
class EventTypeColumnComponent extends MultiselectComboboxColumnComponent(new DatabaseEntityUpdatedList[EventType](classOf[EventType])) {
  override def translateItem(where: StringBuilder, item: AnyRef) = item match {
    case eventType: EventType => where.append(eventType.getId)
    case _ => super.translateItem(where, item)
  }
}
