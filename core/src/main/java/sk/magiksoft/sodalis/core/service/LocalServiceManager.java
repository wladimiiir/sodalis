package sk.magiksoft.sodalis.core.service;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import sk.magiksoft.sodalis.core.PropertyHolder;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.logger.LoggerManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
        final URL configuration = SodalisApplication.get().getConfigurationURL();

        try {
            Document xmlDocument = new SAXBuilder().build(configuration);
            Element services = xmlDocument.getRootElement().getChild("services");
            String serviceClass;
            Service service;

            for (int i = 0; i < services.getChildren().size(); i++) {
                Element serviceElement = services.getChildren().get(i);

                serviceClass = serviceElement.getTextTrim();

                try {
                    service = (Service) Class.forName(serviceClass).newInstance();
                    serviceMap.put(service.getServiceName(), service);
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
                    LoggerManager.getInstance().error(LocalServiceManager.class, ex);
                }
            }
        } catch (JDOMException | IOException ex) {
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
