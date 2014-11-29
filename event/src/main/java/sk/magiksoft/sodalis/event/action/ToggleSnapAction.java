package sk.magiksoft.sodalis.event.action;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.event.settings.EventSettings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author wladimiiir
 */
public class ToggleSnapAction extends AbstractAction implements PropertyChangeListener {
    private final EventSettings settings = EventSettings.getInstance();
    private boolean snapEnabled;

    public ToggleSnapAction() {
        snapEnabled = settings.getBoolean(EventSettings.B_SNAP_ENABLED);
        refreshName();
        settings.addPropertyChangeListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean snabEnabled = !(Boolean) settings.getValue(EventSettings.B_SNAP_ENABLED);

        settings.setValue(EventSettings.B_SNAP_ENABLED, snabEnabled);
        settings.save();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!evt.getPropertyName().equals(EventSettings.B_SNAP_ENABLED)) {
            return;
        }
        snapEnabled = (Boolean) evt.getNewValue();
        refreshName();
    }

    private void refreshName() {
        if (snapEnabled) {
            this.putValue(NAME, "<html><b>F9</b> " + LocaleManager.getString("toogleSnap.off") + "</html>");
        } else {
            this.putValue(NAME, "<html><b>F9</b> " + LocaleManager.getString("toogleSnap.on") + "</html>");
        }
    }

}
