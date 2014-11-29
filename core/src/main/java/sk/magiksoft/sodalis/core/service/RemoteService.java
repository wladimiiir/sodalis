package sk.magiksoft.sodalis.core.service;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author wladimiiir
 */
public interface RemoteService extends Service, Remote, Serializable {
    void registerRemoteServiceListener(String clientHostName, String listenerRemoteName) throws RemoteException;
}
