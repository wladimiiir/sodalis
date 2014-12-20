package sk.magiksoft.sodalis.core.service;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import sk.magiksoft.sodalis.core.PropertyHolder;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wladimiiir
 */
public class ClientServiceManager implements ServiceManager {

    private String serverLocation;
    private String clientLocation;

    private Map<String, ServiceListener> serviceListenersMap = new HashMap<String, ServiceListener>();
    private Map<String, LocalService> localServiceMap = new HashMap<String, LocalService>();

    public ClientServiceManager() {
        initServerClientLocations();
        loadLocalServices();
    }

    @Override
    public Service getService(String serviceName) {
        if (localServiceMap.containsKey(serviceName)) {
            return localServiceMap.get(serviceName);
        }

        try {
            Registry registry = LocateRegistry.getRegistry(serverLocation);

            while (true) {
                try {
                    return (RemoteService) registry.lookup(serviceName);
                } catch (Exception e) {
                    int result = ISOptionPane.showConfirmDialog(SodalisApplication.get().getMainFrame(),
                            LocaleManager.getString("serverNotAvailableTryAgain"),
                            LocaleManager.getString("error"),
                            ISOptionPane.YES_NO_OPTION);
                    if (result == ISOptionPane.NO_OPTION) {
                        System.exit(0);
                    }
                }
            }
        } catch (AccessException ex) {
            LoggerManager.getInstance().error(ClientServiceManager.class, ex);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientServiceManager.class, ex);
        }

        return null;
    }

    @Override
    public void addServiceListener(String serviceName, ServiceListener listener) {
        try {
            Service service = getService(serviceName);
            if (service == null) {
                return;
            }

            if (service instanceof RemoteService) {
                String identification = ServiceListener.IDENTIFICATION + "$" + System.currentTimeMillis();
                DelegateRemoteServiceListener remoteServiceListener = new DelegateRemoteServiceListener(identification);
                Registry registry;

                try {
                    registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
                } catch (ExportException e) {
                    registry = LocateRegistry.getRegistry(clientLocation);
                }

                registry.rebind(identification, remoteServiceListener);
                serviceListenersMap.put(identification, listener);
                ((RemoteService) service).registerRemoteServiceListener(clientLocation, identification);
            } else if (service instanceof LocalService) {
                ((LocalService) service).registerServiceListener(listener);
            }
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientServiceManager.class, ex);
        }
    }

    @Override
    public void applicationWillExit() {
        for (LocalService localService : localServiceMap.values()) {
            localService.applicationWillExit();
        }
    }

    @Override
    public void applicationOpened() {
        for (LocalService localService : localServiceMap.values()) {
            localService.applicationOpened();
        }
    }

    @Override
    public void initialize() {
        for (LocalService localService : localServiceMap.values()) {
            localService.initialize();
        }
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
                    if (service instanceof LocalService) {
                        localServiceMap.put(service.getServiceName(), (LocalService) service);
                    }
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
                    LoggerManager.getInstance().error(LocalServiceManager.class, ex);
                }
            }
        } catch (JDOMException | IOException ex) {
            LoggerManager.getInstance().error(PropertyHolder.class, ex);
        }
    }


    public void actionPerformed(String serviceListenerIdentification, ServiceEvent event) {
        if (serviceListenersMap.containsKey(serviceListenerIdentification)) {
            serviceListenersMap.get(serviceListenerIdentification).actionPerformed(event);
        }
    }

    private void initServerClientLocations() {
        serverLocation = SodalisApplication.getProperty(PropertyHolder.SERVER_LOCATION, "127.0.0.1");
        clientLocation = SodalisApplication.getProperty(PropertyHolder.CLIENT_LOCATION, "127.0.0.1");
    }
}
