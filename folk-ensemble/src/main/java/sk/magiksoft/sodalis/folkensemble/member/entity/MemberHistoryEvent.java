
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.folkensemble.member.entity;

import sk.magiksoft.sodalis.core.history.HistoryEvent;
import sk.magiksoft.sodalis.core.locale.LocaleManager;

/**
 * @author wladimiiir
 */
public class MemberHistoryEvent extends HistoryEvent {
    public static final int DEACTIVATE = 4;
    public static final int ACTIVATE = 5;

    public MemberHistoryEvent() {
    }

    public MemberHistoryEvent(int action) {
        super(action);

        switch (action) {
            case DEACTIVATE:
                this.actionName = LocaleManager.getString("deactivated");
                break;
            case ACTIVATE:
                this.actionName = LocaleManager.getString("activated");
                break;
        }
    }
}