package sk.magiksoft.sodalis.core.enumeration;

import sk.magiksoft.sodalis.core.settings.SettingsPanel;

/**
 * @author wladimiiir
 * @since 2011/1/5
 */
public class ReferenceEnumerationInfo implements EnumerationInfo {
    @Override
    public Class<? extends EnumerationEntry> getEnumerationEntryClass() {
        return ReferenceEnumerationEntry.class;
    }

    @Override
    public SettingsPanel getSettingsPanel(Enumeration enumeration) {
        return null;
    }
}
