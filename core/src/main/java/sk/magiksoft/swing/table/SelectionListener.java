
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.swing.table;

import java.util.EventListener;

/**
 * @author wladimiiir
 */
public interface SelectionListener extends EventListener {
    boolean selectionWillBeChanged();
}