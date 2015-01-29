package sk.magiksoft.sodalis.core.controlpanel;

import sk.magiksoft.sodalis.core.data.DataListener;

import javax.swing.*;
import java.util.List;

/**
 * @author wladimiiir
 */
public interface ControlPanel extends DataListener {

    void cancelEditing();

    boolean isEditing();

    boolean doUpdate();

    void setupControlPanel(Object object);

    void setAdditionalObjects(List<Object> objects);

    JComponent getControlComponent();

    void setSelectedInfoPanelClass(Class<? extends InfoPanel> infoPanelClass);

    Class<? extends InfoPanel> getSelectedInfoPanelClass();
}
