package sk.magiksoft.sodalis.core.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @author wladimiiir
 */
public class CalendarUtils {
    public static boolean dayEquals(Calendar c1, Calendar c2) {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                && c1.get(Calendar.DATE) == c2.get(Calendar.DATE);
    }

    public static Calendar getCalendar(Date value) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(value);
        return cal;
    }

    public static boolean monthEquals(Calendar c1, Calendar c2) {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
    }

    public static boolean weekEquals(Calendar c1, Calendar c2) {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR);
    }
}
