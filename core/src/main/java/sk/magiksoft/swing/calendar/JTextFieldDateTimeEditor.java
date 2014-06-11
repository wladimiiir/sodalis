
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.swing.calendar;

import com.toedter.calendar.DateUtil;
import com.toedter.calendar.JTextFieldDateEditor;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 12/2/10
 * Time: 10:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class JTextFieldDateTimeEditor extends JTextFieldDateEditor{
    public JTextFieldDateTimeEditor() {
        initDateUtil();
    }

    public JTextFieldDateTimeEditor(String datePattern, String maskPattern, char placeholder) {
        super(datePattern, maskPattern, placeholder);
        initDateUtil();
    }

    public JTextFieldDateTimeEditor(boolean showMask, String datePattern, String maskPattern, char placeholder) {
        super(showMask, datePattern, maskPattern, placeholder);
        initDateUtil();
    }

    private void initDateUtil() {
        dateUtil = new DateUtil(){
            @Override
            public boolean checkDate(Date date) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                Calendar minCal = Calendar.getInstance();
                minCal.setTime(minSelectableDate);
                minCal.set(Calendar.SECOND, 0);
                minCal.set(Calendar.MILLISECOND, 0);

                Calendar maxCal = Calendar.getInstance();
                maxCal.setTime(maxSelectableDate);
                maxCal.set(Calendar.SECOND, 0);
                maxCal.set(Calendar.MILLISECOND, 0);

                return !(calendar.before(minCal) || calendar.after(maxCal));
            }
        };
    }
}