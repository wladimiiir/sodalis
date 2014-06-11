
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.security.event;

import java.util.EventListener;

/**
 *
 * @author wladimiiir
 */
public interface LoginListener extends  EventListener {
    void subjectLoggedIn(LoginEvent event);
    void subjectLoggedOut(LoginEvent event);
}