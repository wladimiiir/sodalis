package sk.magiksoft.sodalis.core.service;

import java.rmi.RemoteException;

/**
 * @author wladimiiir
 * @since 2010/4/17
 */
public interface Service {
    String getServiceName() throws RemoteException;

    void initialize() throws RemoteException;
}
