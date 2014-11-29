package sk.magiksoft.sodalis.core.service;

/**
 * @author wladimiiir
 */
public interface ServiceManager {
    Service getService(String serviceName);

    void addServiceListener(String serviceName, ServiceListener listener);

    void applicationWillExit();

    void applicationOpened();

    void initialize();
}
