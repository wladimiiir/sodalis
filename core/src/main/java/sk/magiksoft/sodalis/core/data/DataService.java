package sk.magiksoft.sodalis.core.data;

import sk.magiksoft.sodalis.core.data.remote.server.DataManager;
import sk.magiksoft.sodalis.core.service.Service;

import java.rmi.RemoteException;

/**
 * @author wladimiiir
 * @since 2010/5/18
 */
public interface DataService extends Service {
    public static final String SERVICE_NAME = "DataService";

    DataManager getDataManager() throws RemoteException;
}
