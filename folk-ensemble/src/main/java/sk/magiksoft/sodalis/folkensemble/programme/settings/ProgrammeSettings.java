package sk.magiksoft.sodalis.folkensemble.programme.settings;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.printing.TableColumnWrapper;
import sk.magiksoft.sodalis.core.printing.TablePrintSettings;
import sk.magiksoft.sodalis.core.settings.Settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wladimiiir
 */
public class ProgrammeSettings extends Settings {

    private static ProgrammeSettings instance;

    private ProgrammeSettings() {
        super(ProgrammeSettings.class.getName());
        instance = this;
    }

    public static ProgrammeSettings getInstance() {
        if (instance == null) {
            new ProgrammeSettings();
        }
        return instance;
    }

    @Override
    protected Map<String, Object> getDefaultSettingsMap() {
        Map<String, Object> settings = new HashMap<String, Object>();

        settings.put(O_SELECTED_CATEGORIES, new ArrayList());
        settings.put(O_DEFAULT_PRINT_SETTINGS, getDefaultPrintSettings());
        settings.put(O_USER_PRINT_SETTINGS, new ArrayList());

        return settings;
    }

    private Object getDefaultPrintSettings() {
        TablePrintSettings printSettings;
        List<TableColumnWrapper> tableColumns = new ArrayList<TableColumnWrapper>();
        TableColumnWrapper tableColumn;

        tableColumn = new TableColumnWrapper("programmeName", LocaleManager.getString("programmeName"), 75);
        tableColumns.add(tableColumn);
        tableColumn = new TableColumnWrapper("description", LocaleManager.getString("description"), 75);
        tableColumns.add(tableColumn);
        tableColumn = new TableColumnWrapper("programmeDuration", LocaleManager.getString("songDuration"), 75);
        tableColumns.add(tableColumn);

        printSettings = new TablePrintSettings("");
        printSettings.setTableColumnWrappers(tableColumns);
        printSettings.setShowPageNumbers(true);
        return printSettings;
    }
}
