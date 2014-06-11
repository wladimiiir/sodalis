
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

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import sk.magiksoft.sodalis.core.PropertyHolder;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.exception.VetoException;
import sk.magiksoft.sodalis.core.logger.LoggerManager;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wladimiiir
 */
public class ServerServiceManager implements ServiceManager {

    private Map<String, RemoteService> serviceMap = new HashMap<String, RemoteService>();
    private String serverLocation;

    public ServerServiceManager() throws VetoException {
        loadLocalServices();
        startServer();

        throw new VetoException("Server ready.");
    }

    @Override
    public Service getService(String serviceName) {
        return serviceMap.get(serviceName);
    }

    @Override
    public void addServiceListener(String serviceName, ServiceListener listener) {
    }

    @Override
    public void applicationWillExit() {
    }

    @Override
    public void applicationOpened() {
    }

    @Override
    public void initialize() {
        for (RemoteService remoteService : serviceMap.values()) {
            try {
                remoteService.initialize();
            } catch (RemoteException e) {
                LoggerManager.getInstance().error(getClass(), e);
            }
        }
    }

    private void loadLocalServices() {
        final File propertyFile = SodalisApplication.get().getConfigurationXMLFile();

        if (!propertyFile.exists()) {
            return;
        }

        try {
            Document xmlDocument = new SAXBuilder().build(propertyFile);
            Element services = xmlDocument.getRootElement().getChild("services");
            String serviceClass;
            Service service;

            serverLocation = SodalisApplication.getProperty(PropertyHolder.SERVER_LOCATION, "127.0.0.1");
            for (int i = 0; i < services.getChildren().size(); i++) {
                Element serviceElement = (Element) services.getChildren().get(i);

                serviceClass = serviceElement.getTextTrim();

                try {
                    service = (Service) Class.forName(serviceClass).newInstance();

                    if (service instanceof RemoteService) {
                        serviceMap.put(service.getServiceName(), (RemoteService) service);
                    }
                } catch (InstantiationException ex) {
                    LoggerManager.getInstance().error(LocalServiceManager.class, ex);
                } catch (IllegalAccessException ex) {
                    LoggerManager.getInstance().error(LocalServiceManager.class, ex);
                } catch (ClassNotFoundException ex) {
                    LoggerManager.getInstance().error(LocalServiceManager.class, ex);
                }
            }
        } catch (JDOMException ex) {
            LoggerManager.getInstance().error(PropertyHolder.class, ex);
        } catch (IOException ex) {
            LoggerManager.getInstance().error(PropertyHolder.class, ex);
        }
    }

    private void startServer() {
        try {
            System.out.println("Creating a RMI registry on adress " + serverLocation + " on the default port (1099).");
            Registry localRegistry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

            for (Map.Entry<String, RemoteService> entry : serviceMap.entrySet()) {
                System.out.println("Publishing service \"" + entry.getKey() + "\" in local registry.");
                localRegistry.rebind(entry.getKey(), entry.getValue());
                System.out.println("Published service \"" + entry.getKey() + "\".");
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}