
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.ui.controlpanel;

/**
 * @author wladimiiir
 */
public class InfoPanelStateEvent {
    public static final int STATE_TYPE_UPDATE = 1;
    public static final int STATE_TYPE_VALIDITY = 2;

    private int stateType;
    private boolean stateValue;

    public InfoPanelStateEvent(int stateType, boolean stateValue) {
        this.stateType = stateType;
        this.stateValue = stateValue;
    }

    public boolean getStateValue() {
        return stateValue;
    }

    public int getStateType() {
        return stateType;
    }
}