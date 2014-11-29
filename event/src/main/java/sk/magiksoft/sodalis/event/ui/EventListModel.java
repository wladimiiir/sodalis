package sk.magiksoft.sodalis.event.ui;

import sk.magiksoft.sodalis.event.entity.Event;
import sk.magiksoft.swing.list.ObjectListModel;

import java.util.Comparator;

/**
 * @author wladimiiir
 */
public class EventListModel extends ObjectListModel<Event> {
    private static final Comparator<Event> EVENT_COMPARATOR = new Comparator<Event>() {

        public int compare(Event o1, Event o2) {
            return o1.getStartTime().compareTo(o2.getStartTime());
        }
    };

    @Override
    protected Comparator<Event> getComparator() {
        return EVENT_COMPARATOR;
    }
}
