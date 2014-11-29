package sk.magiksoft.sodalis.core.data.remote.server.impl;

import sk.magiksoft.sodalis.core.data.DataListener;
import sk.magiksoft.sodalis.core.data.remote.server.DataManager;
import sk.magiksoft.sodalis.core.data.remote.server.DataRemoteService;
import sk.magiksoft.sodalis.core.data.remote.server.DataServiceEvent;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.service.AbstractRemoteService;

import java.rmi.RemoteException;
import java.util.List;

/**
 * @author wladimiiir
 */
public class DataRemoteServiceImpl extends AbstractRemoteService implements DataRemoteService, DataListener {

    private DataManager dataManager;

    public DataRemoteServiceImpl() throws RemoteException {
        try {
            dataManager = new DataManagerImpl();
            ((DataManagerImpl) dataManager).addDataListener(this);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(DataRemoteServiceImpl.class, ex);
        }
    }

    @Override
    public DataManager getDataManager() throws RemoteException {
        return dataManager;
    }

    @Override
    public String getServiceName() throws RemoteException {
        return SERVICE_NAME;
    }

    @Override
    public void entitiesAdded(List<? extends DatabaseEntity> entities) {
        fireServiceEvent(new DataServiceEvent(DataServiceEvent.ACTION_RECORDS_ADDED, entities));
    }

    @Override
    public void entitiesUpdated(List<? extends DatabaseEntity> entities) {
        fireServiceEvent(new DataServiceEvent(DataServiceEvent.ACTION_RECORDS_UPDATED, entities));
    }

    @Override
    public void entitiesRemoved(List<? extends DatabaseEntity> entities) {
        fireServiceEvent(new DataServiceEvent(DataServiceEvent.ACTION_RECORDS_REMOVED, entities));
    }
}
