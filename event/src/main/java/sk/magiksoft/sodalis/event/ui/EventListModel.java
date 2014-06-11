
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.event.ui;

import java.util.Comparator;
import sk.magiksoft.sodalis.event.entity.Event;
import sk.magiksoft.swing.list.ObjectListModel;

/**
 *
 * @author wladimiiir
 */
public class EventListModel extends ObjectListModel<Event> {
    private static final Comparator<Event> EVENT_COMPARATOR = new Comparator<Event>() {

        public int compare(Event o1, Event o2) {
            return o1.getStartTime().compareTo(o2.getStartTime());
        }
    };
    
    @Override
    protected Comparator<Event> getComparator() {
        return EVENT_COMPARATOR;
    }
}