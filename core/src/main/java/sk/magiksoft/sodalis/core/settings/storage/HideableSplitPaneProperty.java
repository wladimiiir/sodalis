
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.settings.storage;

import org.jdesktop.application.session.SplitPaneProperty;
import org.jdesktop.application.session.SplitPaneState;
import sk.magiksoft.swing.HideableSplitPane;

import java.awt.*;

/**
 * @author wladimiiir
 */
public class HideableSplitPaneProperty extends SplitPaneProperty {

    @Override
    public Object getSessionState(Component c) {
        Object state = super.getSessionState(c);

        if (!(c instanceof HideableSplitPane)
                || !(state instanceof SplitPaneState)) {
            return null;
        }

        return new HideableSplitPaneState((SplitPaneState) state,
                ((HideableSplitPane) c).getState(),
                ((HideableSplitPane) c).getLastDividerLocation());
    }

    @Override
    public void setSessionState(Component c, Object state) {
        if (!(c instanceof HideableSplitPane)
                || !(state instanceof HideableSplitPaneState)) {
            return;
        }

        super.setSessionState(c, state);
        ((HideableSplitPane) c).setState(((HideableSplitPaneState) state).getState());
        ((HideableSplitPane) c).setLastDividerLocation(((HideableSplitPaneState) state).getLastDividerLocation());
    }

}