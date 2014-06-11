
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.data.remote.server;

import sk.magiksoft.sodalis.core.data.remote.server.impl.DataRemoteServiceImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author wladimiiir
 */
public class SodalisServerStarter {

    public static void main(String[] args) {
        try {
            System.out.println("Creating a local RMI registry on the default port.");
            Registry localRegistry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            DataRemoteService dataService = new DataRemoteServiceImpl();
            System.out.println("Publishing service \"" + DataRemoteService.SERVICE_NAME + "\" in local registry.");
            localRegistry.rebind(DataRemoteService.SERVICE_NAME, dataService);
            System.out.println("Published DataRemoteService as service \"" + DataRemoteService.SERVICE_NAME + "\". Ready.");
        } catch (RemoteException e) {
            System.out.println("Problem with remote object, exception: \n" + e);
        }
    }
}
