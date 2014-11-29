package sk.magiksoft.sodalis.core.service;

import java.io.Serializable;

/**
 * @author wladimiiir
 */
public class ServiceEvent implements Serializable {
    private int action;

    public ServiceEvent(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }
}
