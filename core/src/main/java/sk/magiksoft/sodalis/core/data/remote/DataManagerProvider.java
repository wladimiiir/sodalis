
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.data.remote;

import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.data.DataListener;
import sk.magiksoft.sodalis.core.data.DataService;
import sk.magiksoft.sodalis.core.data.remote.server.DataManager;
import sk.magiksoft.sodalis.core.data.remote.server.DataRemoteService;
import sk.magiksoft.sodalis.core.data.remote.server.DataServiceEvent;
import sk.magiksoft.sodalis.core.data.remote.server.impl.DataManagerImpl;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.service.ServiceEvent;
import sk.magiksoft.sodalis.core.service.ServiceListener;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;

/**
 * @author wladimiiir
 */
public class DataManagerProvider implements ServiceListener {

    private static DataManagerProvider instance;
    private DataManager dataManager;
    private final Vector<DataListener> dataListeners = new Vector<DataListener>();

    private DataManagerProvider() {
        try {
            initDataManager();
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(DataManagerProvider.class, ex);
        }
    }

    private static DataManagerProvider getInstance() {
        if (instance == null) {
            instance = new DataManagerProvider();
        }

        return instance;
    }

    public synchronized static DataManager getDataManager() {
        return getInstance().dataManager;
    }

    public synchronized static void addDataListener(DataListener dataListener) {
        synchronized (getInstance().dataListeners) {
            getInstance().dataListeners.add(dataListener);
        }
    }

    protected void initDataManager() throws RemoteException {
        if (Boolean.valueOf(System.getProperty("installation", "FALSE"))) {
            dataManager = new DataManagerImpl();
        } else {
            dataManager = SodalisApplication.get().getService(DataService.class, DataService.SERVICE_NAME).getDataManager();
            SodalisApplication.get().addServiceListener(DataRemoteService.SERVICE_NAME, this);
        }

    }

    private void fireRecordsAdded(List records) {
        synchronized (dataListeners) {
            for (int i = dataListeners.size() - 1; i >= 0; i--) {
                dataListeners.get(i).entitiesAdded(records);
            }
        }
    }

    private void fireRecordsUpdated(List records) {
        synchronized (dataListeners) {
            for (int i = dataListeners.size() - 1; i >= 0; i--) {
                dataListeners.get(i).entitiesUpdated(records);
            }
        }
    }

    private void fireRecordsRemoved(List records) {
        synchronized (dataListeners) {
            for (int i = dataListeners.size() - 1; i >= 0; i--) {
                dataListeners.get(i).entitiesRemoved(records);
            }
        }
    }

    @Override
    public void actionPerformed(ServiceEvent event) {
        if (!(event instanceof DataServiceEvent)) {
            return;
        }

        final DataServiceEvent e = (DataServiceEvent) event;
        switch (e.getAction()) {
            case DataServiceEvent.ACTION_RECORDS_ADDED:
                fireRecordsAdded(e.getRecords());
                break;
            case DataServiceEvent.ACTION_RECORDS_UPDATED:
                fireRecordsUpdated(e.getRecords());
                break;
            case DataServiceEvent.ACTION_RECORDS_REMOVED:
                fireRecordsRemoved(e.getRecords());
                break;
        }
    }
}