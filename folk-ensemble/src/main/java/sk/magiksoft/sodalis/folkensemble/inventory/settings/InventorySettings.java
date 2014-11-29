package sk.magiksoft.sodalis.folkensemble.inventory.settings;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.printing.TableColumnWrapper;
import sk.magiksoft.sodalis.core.printing.TablePrintSettings;
import sk.magiksoft.sodalis.core.settings.Settings;
import sk.magiksoft.sodalis.core.settings.SettingsValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wladimiiir
 */
public class InventorySettings extends Settings {

    @SettingsValue
    public static final String I_BORROWING_DAYS = "borrowingsDays";

    private static InventorySettings instance = null;

    private InventorySettings() {
        super(InventorySettings.class.getName());
        instance = this;
    }

    public static synchronized InventorySettings getInstance() {
        if (instance == null) {
            new InventorySettings();
        }

        return instance;
    }

    @Override
    protected Map<String, Object> getDefaultSettingsMap() {
        Map<String, Object> settings = new HashMap<String, Object>();

        settings.put(I_BORROWING_DAYS, 30);
        settings.put(O_SELECTED_CATEGORIES, new ArrayList());
        settings.put(O_DEFAULT_PRINT_SETTINGS, getDefaultPrintSettings());
        settings.put(O_USER_PRINT_SETTINGS, new ArrayList());

        return settings;
    }

    private Object getDefaultPrintSettings() {
        TablePrintSettings printSettings;
        List<TableColumnWrapper> tableColumns = new ArrayList<TableColumnWrapper>();
        TableColumnWrapper tableColumn;
        int i = 0;

        tableColumn = new TableColumnWrapper("name", LocaleManager.getString("name"), 75);
        tableColumns.add(tableColumn);
        tableColumn = new TableColumnWrapper("evidenceNumber", LocaleManager.getString("evidenceNumber"), 75);
        tableColumns.add(tableColumn);
        tableColumn = new TableColumnWrapper("description", LocaleManager.getString("description"), 75);
        tableColumns.add(tableColumn);
        tableColumn = new TableColumnWrapper("inventoryItem.state", LocaleManager.getString("inventoryItem.state"), 75);
        tableColumns.add(tableColumn);

        printSettings = new TablePrintSettings("");
        printSettings.setTableColumnWrappers(tableColumns);
        printSettings.setShowPageNumbers(true);
        return printSettings;
    }
}
