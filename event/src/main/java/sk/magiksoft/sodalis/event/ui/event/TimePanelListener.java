
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.event.ui.event;

import sk.magiksoft.sodalis.event.entity.Event;

/**
 * @author wladimiiir
 */
public interface TimePanelListener extends java.util.EventListener {
    public void eventAdded(Event event);

    public void eventUpdated(Event event);

    public void eventRemoved(Event event);

    public boolean eventWillBeSelected(Event event);

    public void eventSelected(Event event);

    public void eventDoubleClicked(Event event);
}