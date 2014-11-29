package sk.magiksoft.sodalis.core.enumeration;

import sk.magiksoft.sodalis.core.settings.SettingsPanel;

/**
 * @author wladimiiir
 */
public class DefaultEnumerationInfo implements EnumerationInfo {

    private static final long serialVersionUID = -1l;

    private transient DefaultEnumerationSettingPanel settingPanel;

    @Override
    public Class<? extends EnumerationEntry> getEnumerationEntryClass() {
        return EnumerationEntry.class;
    }

    @Override
    public SettingsPanel getSettingsPanel(Enumeration enumeration) {
        if (settingPanel == null) {
            settingPanel = new DefaultEnumerationSettingPanel(enumeration);
        }

        return settingPanel;
    }

}
