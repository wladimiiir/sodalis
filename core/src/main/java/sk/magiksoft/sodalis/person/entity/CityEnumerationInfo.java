package sk.magiksoft.sodalis.person.entity;

import sk.magiksoft.sodalis.core.enumeration.Enumeration;
import sk.magiksoft.sodalis.core.enumeration.EnumerationEntry;
import sk.magiksoft.sodalis.core.enumeration.EnumerationInfo;
import sk.magiksoft.sodalis.core.settings.SettingsPanel;
import sk.magiksoft.sodalis.person.ui.CityEnumerationSettingsPanel;

/**
 * @author wladimiiir
 * @since 2010/11/17
 */
public class CityEnumerationInfo implements EnumerationInfo {
    private static final long serialVersionUID = -1l;

    private transient SettingsPanel settingPanel;

    @Override
    public Class<? extends EnumerationEntry> getEnumerationEntryClass() {
        return CityEnumerationEntry.class;
    }

    @Override
    public SettingsPanel getSettingsPanel(Enumeration enumeration) {
        if (settingPanel == null) {
            settingPanel = new CityEnumerationSettingsPanel(enumeration);
        }

        return settingPanel;
    }
}
