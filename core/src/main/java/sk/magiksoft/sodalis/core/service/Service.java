
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/


package sk.magiksoft.sodalis.core.service;

import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Apr 17, 2010
 * Time: 3:03:20 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Service {
    String getServiceName() throws RemoteException;

    void initialize() throws RemoteException;
}