package sk.magiksoft.sodalis.core.controlpanel;

import java.util.EventListener;

/**
 * @author wladimiiir
 */
public interface InfoPanelListener extends EventListener {
    void stateChanged(InfoPanelStateEvent event);
}
