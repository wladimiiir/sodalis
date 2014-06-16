
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.context;

import sk.magiksoft.sodalis.core.filter.ui.FilterPanel;

import java.awt.*;

/**
 * @author wladimiiir
 */
public interface ContextManager {
    public Component getMainComponent();

    public Component getStatusPanel();

    public Context getContext();

    public void reloadData();

    boolean isContextInitialized();

    FilterPanel getFilterPanel();
}