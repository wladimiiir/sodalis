
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.exception;

/**
 *
 * @author wladimiiir
 */
public class VetoException extends RuntimeException {

    public VetoException(Throwable cause) {
        super(cause);
    }

    public VetoException(String message, Throwable cause) {
        super(message, cause);
    }

    public VetoException(String message) {
        super(message);
    }

    public VetoException() {
    }

    
}