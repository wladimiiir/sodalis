
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
import sk.magiksoft.sodalis.core.logger.LoggerManager;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wladimiiir
 */
public class LocalServiceManager implements ServiceManager {

    private Map<String, Service> serviceMap = new HashMap<String, Service>();

    public LocalServiceManager() {
        loadLocalServices();
    }

    @Override
    public Service getService(String serviceName) {
        return serviceMap.get(serviceName);
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

            for (int i = 0; i < services.getChildren().size(); i++) {
                Element serviceElement = (Element) services.getChildren().get(i);

                serviceClass = serviceElement.getTextTrim();

                try {
                    service = (Service) Class.forName(serviceClass).newInstance();
                    serviceMap.put(service.getServiceName(), service);
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

    @Override
    public void addServiceListener(String serviceName, ServiceListener listener) {
        final Service service = getService(serviceName);

        if (service instanceof LocalService) {
            ((LocalService) service).registerServiceListener(listener);
        }
    }

    @Override
    public void applicationWillExit() {
        for (Service service : serviceMap.values()) {
            if (service instanceof LocalService) {
                ((LocalService) service).applicationWillExit();
            }
        }
    }

    @Override
    public void applicationOpened() {
        for (Service service : serviceMap.values()) {
            if (service instanceof LocalService) {
                ((LocalService) service).applicationOpened();
            }
        }
    }

    @Override
    public void initialize() {
        for (Service service : serviceMap.values()) {
            try {
                service.initialize();
            } catch (RemoteException e) {
                LoggerManager.getInstance().error(getClass(), e);
            }
        }
    }
}