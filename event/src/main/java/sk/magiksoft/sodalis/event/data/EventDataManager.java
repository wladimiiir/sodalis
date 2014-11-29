package sk.magiksoft.sodalis.event.data;

import org.hibernate.type.CalendarType;
import org.hibernate.type.Type;
import sk.magiksoft.sodalis.core.data.remote.client.ClientDataManager;
import sk.magiksoft.sodalis.event.entity.Event;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * @author wladimiiir
 */
public class EventDataManager extends ClientDataManager {

    private static EventDataManager instance;

    private EventDataManager() {
    }

    public static EventDataManager getInstance() {
        if (instance == null) {
            instance = new EventDataManager();
        }
        return instance;
    }

    public List<Event> getEventsForDay(Calendar date) {
        Calendar startDate = (Calendar) date.clone();
        Calendar endDate;

        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);
        endDate = (Calendar) startDate.clone();
        endDate.add(Calendar.DATE, 1);
        endDate.add(Calendar.MILLISECOND, -1);

        return getEvents(startDate, endDate);
    }

    public List<Event> getEventsForMemberIDs(List<Long> memberIDs) {
        List<Event> events;

        events = getDatabaseEntities("select e from Event e, EnsembleEventData eed, PersonWrapper pw where eed in elements(e.eventDatas) " +
                "and pw in elements(eed.participants) and pw.person.id in (:ids)",
                new String[]{"ids"},
                new Collection[]{memberIDs});

        return events;
    }

    public List<Event> getEvents(Collection<Long> programmeIDs) {
        List<Event> events;

        events = getDatabaseEntities("select e from Event e, EnsembleEventData eed where eed in elements(e.eventDatas) " +
                "and eed.programme.id in (:ids)",
                new String[]{"ids"},
                new Collection[]{programmeIDs});

        return events;
    }

    public List<Event> getEventsForWeek(Calendar date) {
        Calendar startDate = (Calendar) date.clone();
        Calendar endDate;

        while (startDate.get(Calendar.DAY_OF_WEEK) != startDate.getFirstDayOfWeek()) {
            startDate.add(Calendar.DATE, -1);
        }
        endDate = (Calendar) startDate.clone();
        endDate.add(Calendar.DATE, 6);

        return getEventsInDate(startDate, endDate);
    }

    public List<Event> getEventsForMonth(Calendar date) {
        Calendar startDate = (Calendar) date.clone();
        Calendar endDate;

        startDate.set(Calendar.DATE, 1);
        endDate = (Calendar) startDate.clone();
        endDate.add(Calendar.MONTH, 1);
        endDate.add(Calendar.DATE, -1);

        return getEventsInDate(startDate, endDate);
    }

    private List<Event> getEvents(Calendar startDate, Calendar endDate) {
        final List<Event> events = getDatabaseEntities("from Event as event where (event.startTime>=? and event.endTime<=?) " +
                "or (event.repeatMask<>0 and event.repeatStart<=? and event.repeatEnd>=?)",
                new Object[]{startDate, endDate, endDate, startDate},
                new Type[]{CalendarType.INSTANCE, CalendarType.INSTANCE, CalendarType.INSTANCE, CalendarType.INSTANCE});

        return events;
    }

    public List<Event> getEventsInDate(Calendar startDate, Calendar endDate) {
        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);

        endDate.set(Calendar.HOUR_OF_DAY, 23);
        endDate.set(Calendar.MINUTE, 59);
        endDate.set(Calendar.SECOND, 59);
        endDate.set(Calendar.MILLISECOND, 999);

        return getEvents(startDate, endDate);
    }

    public void removeEvent(Event event) {
        removeDatabaseEntity(event);
    }

}
