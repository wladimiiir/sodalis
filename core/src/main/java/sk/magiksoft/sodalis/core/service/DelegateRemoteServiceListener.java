package sk.magiksoft.sodalis.core.service;

import sk.magiksoft.sodalis.core.SodalisApplication;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author wladimiiir
 */
public class DelegateRemoteServiceListener extends UnicastRemoteObject implements RemoteServiceListener, Serializable {

    private String serviceListenerIdentification;

    public DelegateRemoteServiceListener(String serviceListenerIdentification) throws RemoteException {
        this.serviceListenerIdentification = serviceListenerIdentification;
    }

    @Override
    public void actionPerformed(ServiceEvent event) {
        ServiceManager serviceManager = SodalisApplication.get().getServiceManager();

        if (serviceManager instanceof ClientServiceManager) {
            ((ClientServiceManager) serviceManager).actionPerformed(serviceListenerIdentification, event);
        }
    }
}
