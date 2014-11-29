package sk.magiksoft.sodalis.core.ui.controlpanel;

import java.util.EventListener;

/**
 * @author wladimiiir
 */
public interface InfoPanelListener extends EventListener {
    void stateChanged(InfoPanelStateEvent event);
}
