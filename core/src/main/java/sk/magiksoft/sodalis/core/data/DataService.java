
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.core.data;

import sk.magiksoft.sodalis.core.data.remote.server.DataManager;
import sk.magiksoft.sodalis.core.service.Service;

import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: May 18, 2010
 * Time: 6:24:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataService extends Service {
    public static final String SERVICE_NAME = "DataService";

    DataManager getDataManager() throws RemoteException;
}