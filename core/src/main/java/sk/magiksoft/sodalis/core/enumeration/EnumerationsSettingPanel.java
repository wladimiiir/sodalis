package sk.magiksoft.sodalis.core.enumeration;

import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.exception.VetoException;
import sk.magiksoft.sodalis.icon.IconManager;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.settings.SettingsPanel;

import javax.security.auth.Subject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class EnumerationsSettingPanel extends JPanel implements SettingsPanel {
    private static final File ENUMERATION_CFG_FILE = new File("config/enumerations.cfg");

    private JTabbedPane tbpEnumerations;
    private JButton btnImport;
    private JButton btnExport;
    private List<String> visibleEnumerations = null;
    private List<SettingsPanel> enumerationsSettingsPanels = new ArrayList<SettingsPanel>();

    public EnumerationsSettingPanel() {
        initVisibleEnumerations();
        initComponents();
    }

    private void initVisibleEnumerations() {
        if (!ENUMERATION_CFG_FILE.exists()) {
            return;
        }

        try {
            final BufferedReader reader = new BufferedReader(new FileReader(ENUMERATION_CFG_FILE));
            String line;

            visibleEnumerations = new LinkedList<String>();
            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                visibleEnumerations.add(line);
            }

            reader.close();
        } catch (FileNotFoundException e) {
            LoggerManager.getInstance().error(getClass(), e);
        } catch (IOException e) {
            LoggerManager.getInstance().error(getClass(), e);
        }
    }

    private void initComponents() {
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        tbpEnumerations = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        btnImport = new JButton(new ImportEnumerationsAction());
        btnExport = new JButton(new ExportEnumerationsAction());

        pnlButtons.add(btnImport);
        pnlButtons.add(btnExport);
        setLayout(new BorderLayout());
        add(tbpEnumerations, BorderLayout.CENTER);
        add(pnlButtons, BorderLayout.SOUTH);

        reloadEnumerations();
    }

    private void reloadEnumerations() {
        List<Enumeration> enumerations = EnumerationFactory.getInstance().getEnumerations();
        SettingsPanel settingsPanel;

        enumerationsSettingsPanels.clear();
        tbpEnumerations.removeAll();
        for (Enumeration enumeration : enumerations) {
            if (!visibleEnumerations.contains(enumeration.getName())) {
                continue;
            }
            settingsPanel = enumeration.getEnumerationInfo().getSettingsPanel(enumeration);
            if (settingsPanel == null) {
                continue;
            }
            enumerationsSettingsPanels.add(settingsPanel);
            tbpEnumerations.addTab(settingsPanel.getSettingsPanelName(),
                    settingsPanel.getSwingComponent());
        }
    }

    @Override
    public String getSettingsPanelName() {
        return LocaleManager.getString("enumerations");
    }

    @Override
    public JComponent getSwingComponent() {
        return this;
    }

    @Override
    public void reloadSettings() {
        for (SettingsPanel settingsPanel : enumerationsSettingsPanels) {
            settingsPanel.reloadSettings();
        }
    }

    @Override
    public boolean saveSettings() {
        for (SettingsPanel settingsPanel : enumerationsSettingsPanels) {
            if (!settingsPanel.saveSettings()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setup(Subject subject) throws VetoException {
    }

    private class ImportEnumerationsAction extends AbstractAction {

        public ImportEnumerationsAction() {
            super(LocaleManager.getString("import"), IconManager.getInstance().getIcon("import"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SodalisApplication.get().showMessage(LocaleManager.getString("toBeImplemented"));
        }
    }

    private class ExportEnumerationsAction extends AbstractAction {

        public ExportEnumerationsAction() {
            super(LocaleManager.getString("export"), IconManager.getInstance().getIcon("export"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SodalisApplication.get().showMessage(LocaleManager.getString("toBeImplemented"));
        }
    }
}
