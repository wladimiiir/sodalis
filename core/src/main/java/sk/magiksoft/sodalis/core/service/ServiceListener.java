package sk.magiksoft.sodalis.core.service;

/**
 * @author wladimiiir
 */
public interface ServiceListener {
    public static final String IDENTIFICATION = "ServiceListener";

    void actionPerformed(ServiceEvent event);
}
