package sk.magiksoft.sodalis.core.enumeration;

import sk.magiksoft.sodalis.core.settings.SettingsPanel;

import java.io.Serializable;

/**
 * @author wladimiiir
 */
public interface EnumerationInfo extends Serializable {
    Class<? extends EnumerationEntry> getEnumerationEntryClass();

    public SettingsPanel getSettingsPanel(Enumeration enumeration);
}
