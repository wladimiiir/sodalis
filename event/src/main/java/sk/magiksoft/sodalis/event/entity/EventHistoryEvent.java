
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.event.entity;

import java.util.Calendar;
import sk.magiksoft.sodalis.core.history.HistoryEvent;

/**
 *
 * @author wladimiiir
 */
public class EventHistoryEvent extends HistoryEvent{

    private Event event;

    public EventHistoryEvent(Event event) {
        this.event = event;
        this.date = (Calendar) event.getStartTime().clone();
        this.description = event.getEventName() + event.getEventDuration();
    }

    public Event getEvent() {
        return event;
    }
    
    @Override
    public String getActionName() {
        return event.getEventTypeName();
    }
}