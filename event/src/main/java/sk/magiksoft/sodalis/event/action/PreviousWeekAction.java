
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.event.action;

import sk.magiksoft.sodalis.event.EventContextManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Calendar;

/**
 * @author wladimiiir
 */
public class PreviousWeekAction extends AbstractAction {

    public void actionPerformed(ActionEvent e) {
        EventContextManager.getInstance().addToCalendar(Calendar.DATE, -7);
    }

}