
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.swing.event;

import java.awt.event.ActionEvent;

/**
 * @author wladimiiir
 */
public class PopupTextFieldEvent extends ActionEvent {

    public PopupTextFieldEvent(Object source, int id, String command) {
        super(source, id, command);
    }


}