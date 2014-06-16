
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/


package sk.magiksoft.sodalis.core.data.h2;

import org.hibernate.internal.util.SerializationHelper;
import sk.magiksoft.sodalis.core.enumeration.EnumerationEntry;
import sk.magiksoft.utils.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Aug 5, 2010
 * Time: 9:06:55 AM
 * To change this template use File | Settings | File Templates.
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