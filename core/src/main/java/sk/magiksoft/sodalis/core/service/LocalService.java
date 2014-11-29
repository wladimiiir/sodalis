package sk.magiksoft.sodalis.core.service;

/**
 * @author wladimiiir
 * @since 2010/4/17
 */
public interface LocalService extends Service {
    @Override
    String getServiceName();

    @Override
    void initialize();

    void applicationWillExit();

    void registerServiceListener(ServiceListener listener);

    void applicationOpened();
}
