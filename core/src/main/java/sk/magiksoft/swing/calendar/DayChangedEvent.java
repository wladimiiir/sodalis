
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.swing.calendar;

import java.util.Calendar;
import javax.swing.event.ChangeEvent;

/**
 *
 * @author wladimiiir
 */
public class DayChangedEvent extends ChangeEvent{

    private Calendar oldDay;
    private Calendar newDay;

    public DayChangedEvent(Object source, Calendar oldDay, Calendar newDay) {
        super(source);
        this.oldDay = oldDay;
        this.newDay = newDay;
    }

    public Calendar getNewDay() {
        return newDay;
    }

    public Calendar getOldDay() {
        return oldDay;
    }
}