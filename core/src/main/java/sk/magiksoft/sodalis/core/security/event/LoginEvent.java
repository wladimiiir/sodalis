
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

import javax.security.auth.Subject;
import java.io.Serializable;

/**
 * @author wladimiiir
 */
public class LoginEvent implements Serializable {

    private Subject subject;

    public LoginEvent(Subject subject) {
        this.subject = subject;
    }

    public Subject getSubject() {
        return subject;
    }
}