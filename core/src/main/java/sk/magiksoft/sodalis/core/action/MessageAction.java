
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.action;

import javax.swing.*;
import java.util.List;

/**
 *
 * @author wladimiiir
 */
public abstract class MessageAction extends AbstractAction{

    public MessageAction() {
    }

    public MessageAction(String name) {
        super(name);
    }

    protected MessageAction(Icon icon) {
        super("", icon);
    }

    public MessageAction(String name, Icon icon) {
        super(name, icon);
    }


    public abstract ActionMessage getActionMessage( List objects);
}