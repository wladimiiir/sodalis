package sk.magiksoft.sodalis.core.settings;

import sk.magiksoft.sodalis.core.exception.VetoException;

import javax.security.auth.Subject;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class CompoundSettingsPanel extends JPanel implements SettingsPanel {

    private GridBagConstraints c;
    private String settingsPanelName;
    private List<SettingsPanel> settingsPanels = new ArrayList<SettingsPanel>();

    public CompoundSettingsPanel() {
        initComponents();
    }

    public void setSettingsPanelName(String settingsPanelName) {
        this.settingsPanelName = settingsPanelName;
    }

    @Override
    public String getSettingsPanelName() {
        return settingsPanelName;
    }

    @Override
    public JComponent getSwingComponent() {
        return this;
    }

    public void addSettingsPanel(SettingsPanel panel) {
        JPanel swingPanel = new JPanel(new BorderLayout());

        settingsPanels.add(panel);
        swingPanel.setBorder(new TitledBorder(panel.getSettingsPanelName()));
        swingPanel.add(panel.getSwingComponent(), BorderLayout.CENTER);
        add(swingPanel, c);
        c.gridy++;
    }

    @Override
    public void reloadSettings() {
        for (SettingsPanel settingsPanel : settingsPanels) {
            settingsPanel.reloadSettings();
        }
    }

    @Override
    public boolean saveSettings() {
        for (SettingsPanel settingsPanel : settingsPanels) {
            if (!settingsPanel.saveSettings()) {
                return false;
            }
        }

        return true;
    }

    private void initComponents() {
        c = new GridBagConstraints();
        c.gridx = c.gridy = 0;
        c.weightx = c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        setLayout(new GridBagLayout());
    }

    @Override
    public void setup(Subject subject) throws VetoException {
        for (SettingsPanel settingsPanel : settingsPanels) {
            settingsPanel.setup(subject);
        }
    }
}
