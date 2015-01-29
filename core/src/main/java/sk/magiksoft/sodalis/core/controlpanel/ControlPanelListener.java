package sk.magiksoft.sodalis.core.controlpanel;

import java.util.EventListener;

/**
 * @author wladimiiir
 */
public interface ControlPanelListener extends EventListener {
    void saved();

    void cancelled();
}
