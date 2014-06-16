
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
 * @author wladimiiir
 */
public abstract class AbstractObjectAction extends AbstractAction implements ObjectAction {

    public AbstractObjectAction() {
    }

    public AbstractObjectAction(String name, Icon icon) {
        super(name, icon);
    }

    public AbstractObjectAction(String name) {
        super(name);
    }

    @Override
    public boolean isActionEnabled(List objects) {
        return true;
    }

    @Override
    public boolean isActionShown(List objects) {
        return true;
    }

}