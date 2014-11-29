package sk.magiksoft.sodalis.event.entity;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * @author wladimiiir
 */
public class DateTime {

    private static final DecimalFormat timeFormat = new DecimalFormat("00");
    private int hours;
    private int minutes;
    private int seconds;

    public DateTime(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public DateTime(int hours, int minutes) {
        this(hours, minutes, 0);
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void add(int field, int value) {
        switch (field) {
            case Calendar.HOUR:
                hours += value;
                if (hours < 0) {
                    hours = 0;
                } else if (hours > 23) {
                    hours = 23;
                }
                break;
            case Calendar.MINUTE:
                minutes += value;
                if (minutes < 0) {
                    minutes = 0;
                } else if (minutes > 59) {
                    minutes = 59;
                }
                break;
            case Calendar.SECOND:
                seconds += value;
                if (seconds < 0) {
                    seconds = 0;
                } else if (seconds > 59) {
                    seconds = 59;
                }
                break;

        }
    }

    public boolean before(DateTime time) {
        if (hours < time.hours) {
            return true;
        }
        if (hours > time.hours) {
            return false;
        }
        return minutes < time.minutes;
    }

    public boolean after(DateTime time) {
        if (hours > time.hours) {
            return true;
        }
        if (hours < time.hours) {
            return false;
        }
        return minutes > time.minutes;
    }

    @Override
    public String toString() {
        return timeFormat.format(hours) + ":" + timeFormat.format(minutes);
    }
}
