package sk.magiksoft.sodalis.core.ui.controlpanel;

import java.util.EventListener;

/**
 * @author wladimiiir
 */
public interface ControlPanelListener extends EventListener {
    void saved();

    void cancelled();
}
