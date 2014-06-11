
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.service;

import sk.magiksoft.sodalis.core.logger.LoggerManager;

import java.rmi.AccessException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author wladimiiir
 */
public abstract class AbstractRemoteService extends UnicastRemoteObject implements RemoteService {

    private static final int DEFAULT_CONNECT_TRIAL_COUNT = 5;
    private final Object lockObject = new Object();
    private Vector<ServiceListener> serviceListeners = new Vector<ServiceListener>();
    private Map<String, Vector<String>> remoteServiceListenerMap = new HashMap<String, Vector<String>>();

    public AbstractRemoteService() throws RemoteException {
    }

    @Override
    public void initialize() throws RemoteException {
    }

    @Override
    public void registerRemoteServiceListener(String clientHostName, String listenerRemoteName) throws RemoteException {
        synchronized (lockObject) {
            if (!remoteServiceListenerMap.containsKey(clientHostName)) {
                remoteServiceListenerMap.put(clientHostName, new Vector<String>());
            }
            remoteServiceListenerMap.get(clientHostName).add(listenerRemoteName);
        }
    }

    private void deregisterRemoteServiceListener(String clientHostName, String listenerRemoteName) {
        synchronized (lockObject) {
            Vector<String> listenerRemoteNames = remoteServiceListenerMap.get(clientHostName);

            if (listenerRemoteNames == null) {
                return;
            }
            listenerRemoteNames.remove(listenerRemoteName);
        }
    }

    protected void fireServiceEvent(ServiceEvent event) {
        synchronized (lockObject) {
            for (ServiceListener serviceListener : serviceListeners) {
                serviceListener.actionPerformed(event);
            }

            for (String clientHostName : remoteServiceListenerMap.keySet()) {
                if (remoteServiceListenerMap.get(clientHostName).isEmpty()) {
                    continue;
                }
                fireServiceEvent(event, clientHostName);
            }
        }
    }

    private void fireServiceEvent(ServiceEvent event, String clientHostName) {
        new Thread(new FireServiceEventTask(event, clientHostName, remoteServiceListenerMap.get(clientHostName))).start();
    }

    private class FireServiceEventTask implements Runnable {

        private ServiceEvent serviceEvent;
        private String clientHostName;
        private Vector<String> listenerRemoteNames;

        public FireServiceEventTask(ServiceEvent serviceEvent, String clientHostName, Vector<String> listenerRemoteNames) {
            this.serviceEvent = serviceEvent;
            this.clientHostName = clientHostName;
            this.listenerRemoteNames = listenerRemoteNames;
        }

        @Override
        public void run() {
            try {
                Registry registry = LocateRegistry.getRegistry(clientHostName);
                RemoteServiceListener listener;

                synchronized (lockObject) {
                    for (String listenerRemoteName : listenerRemoteNames) {
                        try {
                            listener = (RemoteServiceListener) registry.lookup(listenerRemoteName);
                            int trialCount = DEFAULT_CONNECT_TRIAL_COUNT;

                            while (trialCount-- > 0) {
                                try {
                                    listener.actionPerformed(serviceEvent);
                                    break;
                                } catch (ConnectException e) {
                                    if (trialCount == 0) {
                                        deregisterRemoteServiceListener(clientHostName, listenerRemoteName);
                                        break;
                                    }
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException ex) {
                                        LoggerManager.getInstance().error(AbstractRemoteService.class, ex);
                                    }
                                }
                            }
                        } catch (NotBoundException ex) {
                            LoggerManager.getInstance().error(AbstractRemoteService.class, ex);
                        } catch (AccessException ex) {
                            LoggerManager.getInstance().error(AbstractRemoteService.class, ex);
                        }
                    }
                }
            } catch (RemoteException ex) {
                LoggerManager.getInstance().error(AbstractRemoteService.class, ex);
            }
        }
    }
}