package sk.magiksoft.sodalis.core.service;

import java.util.Vector;

/**
 * @author wladimiiir
 */
public abstract class AbstractLocalService implements LocalService {

    private Vector<ServiceListener> serviceListeners = new Vector<ServiceListener>();

    public AbstractLocalService() {
    }

    @Override
    public void registerServiceListener(ServiceListener listener) {
        serviceListeners.add(listener);
    }

    protected void fireServiceEvent(ServiceEvent serviceEvent) {
        for (ServiceListener serviceListener : serviceListeners) {
            serviceListener.actionPerformed(serviceEvent);
        }
    }

    @Override
    public void applicationWillExit() {
    }

    @Override
    public void initialize() {
    }

    @Override
    public void applicationOpened() {
    }
}
