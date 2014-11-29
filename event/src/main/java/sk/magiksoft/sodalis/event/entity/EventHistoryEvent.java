package sk.magiksoft.sodalis.event.entity;

import sk.magiksoft.sodalis.core.history.HistoryEvent;

import java.util.Calendar;

/**
 * @author wladimiiir
 */
public class EventHistoryEvent extends HistoryEvent {

    private Event event;

    public EventHistoryEvent(Event event) {
        this.event = event;
        this.date = (Calendar) event.getStartTime().clone();
        this.description = event.getEventName() + event.getEventDuration();
    }

    public Event getEvent() {
        return event;
    }

    @Override
    public String getActionName() {
        return event.getEventTypeName();
    }
}
