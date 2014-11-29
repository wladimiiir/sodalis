package sk.magiksoft.sodalis.person.entity;

import sk.magiksoft.sodalis.core.enumeration.Enumeration;
import sk.magiksoft.sodalis.core.enumeration.EnumerationEntry;
import sk.magiksoft.sodalis.core.enumeration.EnumerationInfo;
import sk.magiksoft.sodalis.core.enumeration.TypedEnumerationEntry;
import sk.magiksoft.sodalis.core.settings.SettingsPanel;
import sk.magiksoft.sodalis.person.ui.PersonTitleEnumerationSettingPanel;

/**
 * @author wladimiiir
 */
public class PersonTitleEnumerationInfo implements EnumerationInfo {
    public static final String TYPE_BEFORE_NAME = "beforeName";
    public static final String TYPE_AFTER_NAME = "afterName";

    private static final long serialVersionUID = -1l;

    private transient SettingsPanel settingsPanel;

    @Override
    public Class<? extends EnumerationEntry> getEnumerationEntryClass() {
        return TypedEnumerationEntry.class;
    }

    @Override
    public SettingsPanel getSettingsPanel(Enumeration enumeration) {
        if (settingsPanel == null) {
            settingsPanel = new PersonTitleEnumerationSettingPanel(enumeration);
        }
        return settingsPanel;
    }

}
