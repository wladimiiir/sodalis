
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.data.local;

import sk.magiksoft.sodalis.core.data.DataListener;
import sk.magiksoft.sodalis.core.data.remote.server.DataManager;
import sk.magiksoft.sodalis.core.data.remote.server.DataServiceEvent;
import sk.magiksoft.sodalis.core.data.remote.server.impl.DataManagerImpl;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.service.AbstractLocalService;

import java.rmi.RemoteException;
import java.util.List;

/**
 * @author wladimiiir
 */
public class DataLocalServiceImpl extends AbstractLocalService implements DataLocalService, DataListener {

    private DataManager dataManager;

    public DataLocalServiceImpl() throws RemoteException {
        try {
            dataManager = new DataManagerImpl();
            ((DataManagerImpl) dataManager).addDataListener(this);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(DataLocalServiceImpl.class, ex);
        }
    }

    @Override
    public DataManager getDataManager() {
        return dataManager;
    }

    @Override
    public String getServiceName() {
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