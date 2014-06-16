
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

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author wladimiiir
 */
public interface RemoteServiceListener extends Remote {
    void actionPerformed(ServiceEvent event) throws RemoteException;
}