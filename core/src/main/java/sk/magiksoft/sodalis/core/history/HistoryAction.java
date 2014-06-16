
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.history;

/**
 * @author wladimiiir
 */
public interface HistoryAction {
    public static final int CREATE = 10;
    public static final int UPDATE = 20;
    public static final int DELETE = 30;
    public static final int UNDELETE = 40;
}