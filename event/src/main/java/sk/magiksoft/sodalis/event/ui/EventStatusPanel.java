
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

import sk.magiksoft.sodalis.event.EventContextManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 *
 * @author wladimiiir
 */
public class EventStatusPanel extends JPanel{

    public EventStatusPanel() {
        initComponents();
    }

    private void initComponents() {
        List<Action> contextActions = EventContextManager.getContextActions();
        JButton button;

        setLayout(new GridLayout(1, contextActions.size()));
        
        for (Action action : contextActions) {
            button = new JButton(action);
            button.setFocusPainted(false);
            add(button);
        }
    }
}