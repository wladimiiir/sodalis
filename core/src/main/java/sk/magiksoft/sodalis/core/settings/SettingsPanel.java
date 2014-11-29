package sk.magiksoft.sodalis.core.settings;

import sk.magiksoft.sodalis.core.exception.VetoException;

import javax.security.auth.Subject;
import javax.swing.*;

/**
 * @author wladimiiir
 */
public interface SettingsPanel {

    String getSettingsPanelName();

    JComponent getSwingComponent();

    void setup(Subject subject) throws VetoException;

    void reloadSettings();

    boolean saveSettings();

}
