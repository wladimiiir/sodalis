package sk.magiksoft.sodalis.event.entity.property

import sk.magiksoft.sodalis.event.entity.Event
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslator

 /**
  * @author wladimiiir
  * @since 2011/2/17
  */
class EventPropertyTranslator extends EntityPropertyTranslator[Event] {
  def getTranslations = List(
    EntityTranslation("eventName", e => Option(e.getEventName)),
    EntityTranslation("place", e => Option(e.getPlace)),
    EntityTranslation("eventType", e => Option(e.getEventTypeName)),
    EntityTranslation("dateFrom", e => Option(e.isRepeating match {
      case true => DateFormat.format(e.getRepeatStart.getTime)
      case false => DateFormat.format(e.getStartTime.getTime)
    })),
    EntityTranslation("dateTo", e => Option(e.isRepeating match {
      case true => DateFormat.format(e.getRepeatEnd.getTime)
      case false => DateFormat.format(e.getEndTime.getTime)
    })),
    EntityTranslation("date1", e => Option(e.isRepeating match {
      case true => DateFormat.format(e.getRepeatStart.getTime) + "-" + DateFormat.format(e.getRepeatEnd.getTime)
      case false => DateFormat.format(e.getStartTime.getTime)
    })),
    EntityTranslation("timeFrom", e => Option(e.isRepeating match {
      case true => TimeFormat.format(e.getRepeatStart.getTime)
      case false => TimeFormat.format(e.getStartTime.getTime)
    })),
    EntityTranslation("timeTo", e => Option(e.isRepeating match {
      case true => TimeFormat.format(e.getRepeatEnd.getTime)
      case false => TimeFormat.format(e.getEndTime.getTime)
    })),
    EntityTranslation("time", e => Option(e.isRepeating match {
      case true => TimeFormat.format(e.getRepeatStart.getTime) + "-" + TimeFormat.format(e.getRepeatEnd.getTime)
      case false => TimeFormat.format(e.getStartTime.getTime) + "-" + TimeFormat.format(e.getEndTime.getTime)
    }))
  )
}
