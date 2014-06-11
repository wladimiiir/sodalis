
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.service;

import java.io.Serializable;

/**
 *
 * @author wladimiiir
 */
public class ServiceEvent implements Serializable{
    private int action;

    public ServiceEvent( int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }
}