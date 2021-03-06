package sk.magiksoft.sodalis.person.utils;

import sk.magiksoft.sodalis.person.entity.PersonWrapper;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author wladimiiir
 */
public class PersonUtils {
    public static final Pattern EMAIL_PATTERN = Pattern.compile("([a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4})");

    public static String personWrappersToString(List<PersonWrapper> personWrappers) {
        StringBuilder result = new StringBuilder();

        for (PersonWrapper personWrapper : personWrappers) {
            if (result.length() > 0) {
                result.append(", ");
            }
            result.append(personWrapper.getPersonName());
        }

        return result.toString();
    }

}
