package sk.magiksoft.sodalis.event.entity;

import sk.magiksoft.sodalis.category.entity.Categorized;
import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.entity.PostCreation;
import sk.magiksoft.sodalis.core.history.Historizable;
import sk.magiksoft.sodalis.core.history.HistoryEvent;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.utils.CalendarUtils;

import java.awt.*;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * @author wladimiiir
 */
public class Event extends AbstractDatabaseEntity implements Categorized, Historizable {

    private static final long serialVersionUID = -701570750714095217l;
    private static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("d.M.yyyy H:mm");
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("d.M.yyyy");
    public static final int REPEAT_NONE = 0;
    public static final int REPEAT_MONDAY = 1;
    public static final int REPEAT_TUESDAY = 2;
    public static final int REPEAT_WEDNESDAY = 4;
    public static final int REPEAT_THURSDAY = 8;
    public static final int REPEAT_FRIDAY = 16;
    public static final int REPEAT_SATURDAY = 32;
    public static final int REPEAT_SUNDAY = 64;
    public static final int REPEAT_EVERYDAY = 127;
    public static final int REPEAT_EVERYWEEK = 1000;
    public static final int REPEAT_EVERYMONTH = 10000;
    private String eventName = "";
    private String place = "";
    private Calendar startTime = Calendar.getInstance();
    private Calendar endTime = Calendar.getInstance();
    private Color color = Color.GREEN;
    private int type = -1;
    private Calendar repeatStart = Calendar.getInstance();
    private Calendar repeatEnd = Calendar.getInstance();
    private int repeatMask = 0;
    private List<Calendar> removedFromRepeating = new LinkedList<Calendar>();
    private EventType eventType;
    private List<Category> categories = new ArrayList<Category>();
    private List<Attendee> attendees = new LinkedList<Attendee>();
    protected Map<Class<? extends EventData>, EventData> eventDatas = new HashMap<Class<? extends EventData>, EventData>();

    public Event() {
    }

    public Event(EventType eventType) {
        this.eventType = eventType;
    }

    public Event(String eventName, Calendar fromCalendar, Calendar toCalendar, Color color) {
        this.eventName = eventName;
        this.startTime = fromCalendar;
        this.endTime = toCalendar;
        this.color = color;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }

    public List<Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Attendee> attendees) {
        this.attendees = attendees;
    }

    public boolean isRemovedFromRepeating(Calendar day) {
        for (Calendar removed : removedFromRepeating) {
            if (removed.get(Calendar.DATE) == day.get(Calendar.DATE)
                    && removed.get(Calendar.MONTH) == day.get(Calendar.MONTH)
                    && removed.get(Calendar.YEAR) == day.get(Calendar.YEAR)) {
                return true;
            }
        }
        return false;
    }

    public void removeFromRepeating(Calendar day) {
        if (isRemovedFromRepeating(day)) {
            return;
        }
        if (removedFromRepeating == null) {
            removedFromRepeating = new LinkedList<Calendar>();
        }
        removedFromRepeating.add(day);
    }

    public List<Calendar> getRemovedFromRepeating() {
        return removedFromRepeating;
    }

    public void setRemovedFromRepeating(List<Calendar> removedFromRepeating) {
        this.removedFromRepeating = removedFromRepeating;
    }

    @PostCreation
    public void initEventDatas(Object... switches) {
        for (Object s : switches) {
            if (s instanceof Class && EventData.class.isAssignableFrom((Class) s)) {
                try {
                    eventDatas.put((Class<? extends EventData>) s, (EventData) ((Class) s).newInstance());
                } catch (InstantiationException ex) {
                    LoggerManager.getInstance().error(Event.class, ex);
                } catch (IllegalAccessException ex) {
                    LoggerManager.getInstance().error(Event.class, ex);
                }
            }
        }
    }

    public boolean isRepeating() {
        return repeatMask != REPEAT_NONE;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endCalendar) {
        this.endTime = endCalendar;
        this.endTime.setFirstDayOfWeek(Calendar.MONDAY);
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public String getEventTypeName() {
        return getEventType() == null ? "" : getEventType().getName();
    }

    @Override
    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
        this.startTime.setFirstDayOfWeek(Calendar.MONDAY);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Calendar getRepeatEnd() {
        return repeatEnd;
    }

    public void setRepeatEnd(Calendar repeatEnd) {
        this.repeatEnd = repeatEnd;
    }

    public int getRepeatMask() {
        return repeatMask;
    }

    public void setRepeatMask(int repeatMask) {
        this.repeatMask = repeatMask;
    }

    public Calendar getRepeatStart() {
        return repeatStart;
    }

    public void setRepeatStart(Calendar repeatStart) {
        this.repeatStart = repeatStart;
    }

    public Map<Class<? extends EventData>, EventData> getEventDatas() {
        return eventDatas;
    }

    public void setEventDatas(Map<Class<? extends EventData>, EventData> eventDatas) {
        this.eventDatas = eventDatas;
    }

    public <T extends EventData> T getEventData(Class<T> clazz) {
        return (T) eventDatas.get(clazz);
    }

    public boolean acceptDay(Calendar day) {
        Calendar c;

        if (repeatMask == REPEAT_NONE) {
            return (!day.before(startTime) && !day.after(endTime)) || CalendarUtils.dayEquals(startTime, day)
                    || CalendarUtils.dayEquals(endTime, day);
        }
        if (repeatStart.after(day) || repeatEnd.before(day) || isRemovedFromRepeating(day)) {
            return false;
        }
        if (repeatMask == REPEAT_EVERYDAY) {
            return true;
        }
        if (repeatMask == REPEAT_EVERYWEEK) {
            if (startTime.get(Calendar.DATE) != endTime.get(Calendar.DATE) || startTime.get(Calendar.MONTH)
                    != endTime.get(Calendar.MONTH) || startTime.get(Calendar.YEAR) != endTime.get(
                    Calendar.YEAR)) {
                c = (Calendar) startTime.clone();
                while (c.get(Calendar.DATE) != endTime.get(Calendar.DATE) || c.get(Calendar.MONTH) != endTime.
                        get(Calendar.MONTH) || c.get(Calendar.YEAR) != endTime.get(Calendar.YEAR)) {
                    if (day.get(Calendar.DAY_OF_WEEK) == c.get(Calendar.DAY_OF_WEEK)) {
                        return true;
                    }
                    c.add(Calendar.DATE, 1);
                }
                return false;
            } else {
                return day.get(Calendar.DAY_OF_WEEK) == startTime.get(Calendar.DAY_OF_WEEK);
            }
        }
        if (repeatMask == REPEAT_EVERYMONTH) {
            if (startTime.get(Calendar.DATE) != endTime.get(Calendar.DATE) || startTime.get(Calendar.MONTH)
                    != endTime.get(Calendar.MONTH) || startTime.get(Calendar.YEAR) != endTime.get(
                    Calendar.YEAR)) {
                c = (Calendar) startTime.clone();
                while (c.get(Calendar.DATE) != endTime.get(Calendar.DATE) || c.get(Calendar.MONTH) != endTime.
                        get(Calendar.MONTH) || c.get(Calendar.YEAR) != endTime.get(Calendar.YEAR)) {
                    if (day.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH)) {
                        return true;
                    }
                    c.add(Calendar.DATE, 1);
                }
                return false;
            } else {
                return day.get(Calendar.DAY_OF_MONTH) == startTime.get(Calendar.DAY_OF_MONTH);
            }
        }
        if (startTime.get(Calendar.DATE) != endTime.get(Calendar.DATE) || startTime.get(Calendar.MONTH)
                != endTime.get(Calendar.MONTH) || startTime.get(Calendar.YEAR) != endTime.get(Calendar.YEAR)) {
            c = (Calendar) day.clone();
            while (!c.before(startTime)) {
                if (((repeatMask & REPEAT_MONDAY) == REPEAT_MONDAY && day.get(Calendar.DAY_OF_WEEK)
                        == Calendar.MONDAY) || ((repeatMask & REPEAT_TUESDAY) == REPEAT_TUESDAY && day.get(
                        Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) || ((repeatMask & REPEAT_WEDNESDAY)
                        == REPEAT_WEDNESDAY && day.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY)
                        || ((repeatMask & REPEAT_THURSDAY) == REPEAT_THURSDAY && day.get(Calendar.DAY_OF_WEEK)
                        == Calendar.THURSDAY) || ((repeatMask & REPEAT_FRIDAY) == REPEAT_FRIDAY && day.get(
                        Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) || ((repeatMask & REPEAT_SATURDAY)
                        == REPEAT_SATURDAY && day.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
                        || ((repeatMask & REPEAT_SUNDAY) == REPEAT_SUNDAY && day.get(Calendar.DAY_OF_WEEK)
                        == Calendar.SUNDAY)) {
                    return true;
                }
                c.add(Calendar.DATE, -1);
            }
            return false;
        } else {
            return ((repeatMask & REPEAT_MONDAY) == REPEAT_MONDAY && day.get(Calendar.DAY_OF_WEEK)
                    == Calendar.MONDAY) || ((repeatMask & REPEAT_TUESDAY) == REPEAT_TUESDAY && day.get(
                    Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) || ((repeatMask & REPEAT_WEDNESDAY)
                    == REPEAT_WEDNESDAY && day.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY)
                    || ((repeatMask & REPEAT_THURSDAY) == REPEAT_THURSDAY && day.get(Calendar.DAY_OF_WEEK)
                    == Calendar.THURSDAY) || ((repeatMask & REPEAT_FRIDAY) == REPEAT_FRIDAY && day.get(
                    Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) || ((repeatMask & REPEAT_SATURDAY)
                    == REPEAT_SATURDAY && day.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || ((repeatMask
                    & REPEAT_SUNDAY) == REPEAT_SUNDAY && day.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
        }
    }

    public boolean acceptWeek(Calendar day) {
        Calendar monday;
        Calendar sunday;

        if (repeatMask == REPEAT_NONE) {
            return CalendarUtils.weekEquals(startTime, day) || CalendarUtils.weekEquals(endTime, day);
        }
        monday = (Calendar) day.clone();
        while (monday.get(Calendar.DAY_OF_WEEK) != monday.getFirstDayOfWeek()) {
            monday.add(Calendar.DATE, -1);
        }
        sunday = (Calendar) day.clone();
        do {
            sunday.add(Calendar.DATE, 1);
        } while (sunday.get(Calendar.DAY_OF_WEEK) != sunday.getFirstDayOfWeek());
        sunday.add(Calendar.DATE, -1);
        if (repeatStart.after(sunday) || repeatEnd.before(monday)) {
            return false;
        }
        if (repeatMask == REPEAT_EVERYMONTH) {
            return monday.get(Calendar.DATE) <= endTime.get(Calendar.DATE) && sunday.get(Calendar.DATE)
                    >= startTime.get(Calendar.DATE);
        }

        return true;
    }

    public boolean acceptMonth(Calendar day) {
        Calendar first;
        Calendar last;

        if (repeatMask == REPEAT_NONE) {
            return CalendarUtils.monthEquals(day, startTime) || CalendarUtils.monthEquals(day, endTime);
        }
        first = (Calendar) day.clone();
        last = (Calendar) day.clone();
        first.set(Calendar.DATE, 1);
        last.set(Calendar.DATE, 1);
        last.add(Calendar.MONTH, 1);
        last.add(Calendar.DATE, -1);
        if (repeatStart.after(last) || repeatEnd.before(first)) {
            return false;
        }

        return true;
    }

    public boolean acceptTime(Calendar time) {
        boolean result;
        Calendar start;
        Calendar end;

        if (!acceptDay(time)) {
            return false;
        }

        result = !time.before(startTime) && !time.after(endTime);
        if (isRepeating()) {
            start = (Calendar) time.clone();
            end = (Calendar) time.clone();
            start.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY));
            start.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));
            end.set(Calendar.HOUR_OF_DAY, endTime.get(Calendar.HOUR_OF_DAY));
            end.set(Calendar.MINUTE, endTime.get(Calendar.MINUTE));
            result |= !time.before(start) && !time.after(end);
        }

        return result;
    }

    public boolean acceptDays(Calendar startDay, int dayCount) {
        Calendar c = (Calendar) startDay.clone();

        for (int i = 0; i < dayCount; i++) {
            if (acceptDay(c)) {
                return true;
            }
            c.add(Calendar.DATE, 1);
        }

        return false;
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof Event) || entity == this) {
            return;
        }
        Event event = (Event) entity;
        this.startTime = (Calendar) event.startTime.clone();
        this.endTime = (Calendar) event.endTime.clone();
        this.repeatStart = event.repeatStart == null ? null : (Calendar) event.repeatStart.clone();
        this.repeatEnd = event.repeatEnd == null ? null : (Calendar) event.repeatEnd.clone();
        this.repeatMask = event.repeatMask;
        this.color = event.color;
        this.eventName = event.eventName;
        this.eventType = event.eventType;
        this.type = event.type;
        this.place = event.place;
        this.attendees.clear();
        this.attendees.addAll(event.attendees);
        this.removedFromRepeating.clear();
        this.removedFromRepeating.addAll(event.removedFromRepeating);
        this.categories.clear();
        this.categories.addAll(event.categories);

        for (EventData eventData : eventDatas.values()) {
            eventData.updateFrom(event.getEventData(eventData.getClass()));
        }
    }

    @Override
    public void clearIDs() {
        super.clearIDs();
        for (Attendee attendee : attendees) {
            attendee.setId(null);
            for (EventData eventData : eventDatas.values()) {
                eventData.clearIDs();
            }
        }
    }

    public String createTooltipText() {
        StringBuilder tooltipText = new StringBuilder();

        tooltipText.append(
                "<html>" +
                        "<head>" +
                        "<h2 align=\"center\">" + getEventName() + "</h2>" +
                        "</head>" +
                        "<body>" +
                        "<table>" +
                        "<tr><td align=\"right\"><b>" + LocaleManager.getString("eventType") + ":" + "&nbsp</b></td><td align=\"left\">" + getEventTypeName() + "</td><tr/>" +
                        "<tr><td align=\"right\"><b>" + LocaleManager.getString("place") + ":" + "&nbsp</b></td><td align=\"left\">" + getPlace() + "</td><tr/>" +
                        "<tr><td align=\"right\"><b>" + LocaleManager.getString("startTime") + ":" + "</b></td><td align=\"left\">" + DATE_TIME_FORMAT.format(getStartTime().getTime()) + "</td><tr/>" +
                        "<tr><td align=\"right\"><b>" + LocaleManager.getString("endTime") + ":" + "&nbsp</b></td><td align=\"left\">" + DATE_TIME_FORMAT.format(getEndTime().getTime()) + "</td><tr/>" +
                        "<tr><td align=\"right\"><b>" + LocaleManager.getString("duration") + ":" + "&nbsp</b></td><td align=\"left\">" + getEventDuration() + "</td><tr/>" +
                        "<tr><td align=\"right\"><b>" + LocaleManager.getString("repetition") + ":" + "&nbsp</b></td><td align=\"left\">" + getRepetition().replaceAll("\\n", "<br/>") + "</td><tr/>" +
                        "</table>" +
                        "</body>" +
                        "</html>");
        return tooltipText.toString();
    }

    @Override
    public String toString() {
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        return dateFormat.format(getStartTime().getTime()) + " - " + dateFormat.format(getEndTime().getTime());
    }

    public String getEventDuration() {
        StringBuilder eventDuration = new StringBuilder();
        long duration = getEndTime().getTimeInMillis() - getStartTime().getTimeInMillis();
        long value;

        //days
        value = duration / (1000 * 60 * 60 * 24);
        if (value > 0) {
            eventDuration.append(value).append(" ");
            if (value == 1) {
                eventDuration.append(LocaleManager.getString("day"));
            } else if (value > 1 && value < 5) {
                eventDuration.append(LocaleManager.getString("days2_4"));
            } else {
                eventDuration.append(LocaleManager.getString("days"));
            }
            eventDuration.append(", ");
        }
        //hours
        value = duration / (1000 * 60 * 60) - value * 24;
        if (value > 0) {
            eventDuration.append(value).append(" ");
            if (value == 1) {
                eventDuration.append(LocaleManager.getString("hour"));
            } else if (value > 1 && value < 5) {
                eventDuration.append(LocaleManager.getString("hours2_4"));
            } else {
                eventDuration.append(LocaleManager.getString("hours"));
            }
            eventDuration.append(", ");
        }
        //minutes
        value = duration / (1000 * 60) - value * 60;
        eventDuration.append(value).append(" ");
        if (value == 1) {
            eventDuration.append(LocaleManager.getString("minute"));
        } else if (value > 1 && value < 5) {
            eventDuration.append(LocaleManager.getString("minutes2_4"));
        } else {
            eventDuration.append(LocaleManager.getString("minutes"));
        }

        return eventDuration.toString();
    }

    private String getRepetition() {
        final StringBuilder repetition;
        final DateFormatSymbols symbols;

        if (repeatMask == REPEAT_NONE) {
            return LocaleManager.getString("noRepetition");
        }
        repetition = new StringBuilder();
        repetition.append(DATE_FORMAT.format(repeatStart.getTime())).append("-").append(DATE_FORMAT.format(repeatEnd.
                getTime())).append("\n");
        if (repeatMask == REPEAT_EVERYDAY) {
            repetition.append(LocaleManager.getString("everyDay"));
        } else if (repeatMask == REPEAT_EVERYWEEK) {
            repetition.append(LocaleManager.getString("everyWeek"));
        } else if (repeatMask == REPEAT_EVERYMONTH) {
            repetition.append(LocaleManager.getString("everyMonth"));
        } else {
            symbols = DateFormatSymbols.getInstance();
            if ((repeatMask & REPEAT_MONDAY) != 0) {
                repetition.append(symbols.getShortWeekdays()[2]).append(", ");
            }
            if ((repeatMask & REPEAT_TUESDAY) != 0) {
                repetition.append(symbols.getShortWeekdays()[3]).append(", ");
            }
            if ((repeatMask & REPEAT_WEDNESDAY) != 0) {
                repetition.append(symbols.getShortWeekdays()[4]).append(", ");
            }
            if ((repeatMask & REPEAT_THURSDAY) != 0) {
                repetition.append(symbols.getShortWeekdays()[5]).append(", ");
            }
            if ((repeatMask & REPEAT_FRIDAY) != 0) {
                repetition.append(symbols.getShortWeekdays()[6]).append(", ");
            }
            if ((repeatMask & REPEAT_SATURDAY) != 0) {
                repetition.append(symbols.getShortWeekdays()[7]).append(", ");
            }
            if ((repeatMask & REPEAT_SUNDAY) != 0) {
                repetition.append(symbols.getShortWeekdays()[1]);
            }
            if (repetition.charAt(repetition.length() - 2) == ',') {
                repetition.delete(repetition.length() - 2, repetition.length());
            }
        }

        return repetition.toString();
    }

    @Override
    public void addHistoryEvent(HistoryEvent event) {
        getEventData(EventHistoryData.class).addHistoryEvent(event);
    }

    @Override
    public List<HistoryEvent> getHistoryEvents(Long entityID) {
        return getEventData(EventHistoryData.class) == null ? Collections.<HistoryEvent>emptyList() : getEventData(EventHistoryData.class).getHistoryEvents();
    }
}
