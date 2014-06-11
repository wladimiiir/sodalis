
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

import java.awt.Point;
import sk.magiksoft.sodalis.event.entity.Event;

/**
 *
 * @author wladimiiir
 */
public interface EventListener {
    public void addEvent(Event event, Point point);
    public void removeEvent(Point point);
    public void removeFromRepeating(Point point);
}