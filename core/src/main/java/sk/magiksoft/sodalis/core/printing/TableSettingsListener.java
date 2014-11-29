package sk.magiksoft.sodalis.core.printing;

/**
 * @author wladimiiir
 */
public interface TableSettingsListener {

    public void tableSettingsDeleted(TablePrintSettings settings);

    void tableSettingsSaved(TablePrintSettings settings);
}
