package sk.magiksoft.sodalis.core.data.h2;

import org.hibernate.internal.util.SerializationHelper;
import sk.magiksoft.sodalis.core.enumeration.EnumerationEntry;
import sk.magiksoft.utils.StringUtils;

/**
 * @author wladimiiir
 * @since 2010/8/5
 */
public class Functions {
    public static String removeDiacritics(String string) {
        return StringUtils.removeDiacritics(string);
    }

    public static String deserializeString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        final Object object = SerializationHelper.deserialize(bytes);

        if (object instanceof String) {
            return (String) object;
        } else if (object instanceof EnumerationEntry) {
            return ((EnumerationEntry) object).getText();
        }

        return "";
    }
}
