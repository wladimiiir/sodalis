
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

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.update.UpdateManager;

/**
 *
 * @author wladimiiir
 */
public class UpdateAction extends AbstractAction{

    public UpdateAction() {
        super(LocaleManager.getString("update"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        UpdateManager.getInstance().doUpdate();
    }

}