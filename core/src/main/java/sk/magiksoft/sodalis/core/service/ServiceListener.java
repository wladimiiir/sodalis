
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

/**
 * @author wladimiiir
 */
public interface ServiceListener {
    public static final String IDENTIFICATION = "ServiceListener";

    void actionPerformed(ServiceEvent event);
}