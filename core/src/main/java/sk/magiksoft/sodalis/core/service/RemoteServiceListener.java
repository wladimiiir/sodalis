package sk.magiksoft.sodalis.core.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author wladimiiir
 */
public interface RemoteServiceListener extends Remote {
    void actionPerformed(ServiceEvent event) throws RemoteException;
}
