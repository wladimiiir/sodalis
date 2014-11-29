package sk.magiksoft.sodalis.event.ui;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.sodalis.event.entity.Event;

/**
 * @author wladimiiir
 */
public class EventTableModel extends ObjectTableModel<Event> {

    public EventTableModel() {
        super(new String[]{
                LocaleManager.getString("eventName"),
                LocaleManager.getString("eventType"),
                LocaleManager.getString("startTime"),
                LocaleManager.getString("endTime")
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Event event = getObject(rowIndex);

        switch (columnIndex) {
            case 0:
                return event.getEventName();
            case 1:
                return event.getEventTypeName();
            case 2:
                return DATE_TIME_FORMAT.format(event.getStartTime().getTime());
            case 3:
                return DATE_TIME_FORMAT.format(event.getEndTime().getTime());
            default:
                return "";
        }
    }
}
