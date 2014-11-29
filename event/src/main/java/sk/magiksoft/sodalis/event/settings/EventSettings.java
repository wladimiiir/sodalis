package sk.magiksoft.sodalis.event.settings;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.printing.TableColumnWrapper;
import sk.magiksoft.sodalis.core.printing.TablePrintSettings;
import sk.magiksoft.sodalis.core.settings.Settings;
import sk.magiksoft.sodalis.core.settings.SettingsManager;
import sk.magiksoft.sodalis.core.settings.SettingsValue;
import sk.magiksoft.sodalis.event.entity.EventType;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author wladimiiir
 */
public class EventSettings extends Settings {
    public static final String B_SNAP_ENABLED = "snapEnabled";
    public static final String I_SNAP_MINUTES = "snapMinutes";
    public static final String I_EVENT_DURATION = "eventDuration";
    public static final String I_DAY_COUNT = "dayCount";
    public static final String O_EVENT_COLOR = "eventColor";
    public static final String L_DEFAULT_EVENT_TYPE_ID = "defaultEventTypeID";
    @SettingsValue(minValue = 0, maxValue = 23)
    public static final String I_START_HOUR = "startHour";
    @SettingsValue(minValue = 1, maxValue = 24)
    public static final String I_END_HOUR = "endHour";

    private static EventSettings instance = null;

    private EventSettings() {
        super(EventSettings.class.getName());
    }

    public static EventSettings getInstance() {
        if (instance == null) {
            instance = new EventSettings();
        }
        return instance;
    }

    @Override
    protected Map<String, Object> getDefaultSettingsMap() {
        Map<String, Object> settingsMap = new HashMap<String, Object>();
        final List<EventType> eventTypes = SettingsManager.getInstance().getDatabaseEntities(EventType.class);

        settingsMap.put(B_SNAP_ENABLED, true);
        settingsMap.put(I_SNAP_MINUTES, 5);
        settingsMap.put(I_EVENT_DURATION, 30);
        settingsMap.put(I_DAY_COUNT, 7);
        settingsMap.put(O_EVENT_COLOR, Color.GREEN);
        settingsMap.put(L_DEFAULT_EVENT_TYPE_ID, eventTypes.isEmpty() ? -1l : eventTypes.get(0).getId());
        settingsMap.put(I_START_HOUR, 7);
        settingsMap.put(I_END_HOUR, 22);
        settingsMap.put(O_SELECTED_CATEGORIES, new ArrayList<>());
        settingsMap.put(O_USER_PRINT_SETTINGS, new ArrayList<>());
        settingsMap.put(O_DEFAULT_PRINT_SETTINGS, createDefaultPrintSettings());

        return settingsMap;
    }

    public TablePrintSettings createDefaultPrintSettings() {
        final List<TableColumnWrapper> settings = new LinkedList<TableColumnWrapper>();

        settings.add(new TableColumnWrapper("eventName", LocaleManager.getString("eventName"), 75));
        settings.add(new TableColumnWrapper("place", LocaleManager.getString("place"), 75));
        settings.add(new TableColumnWrapper("eventType", LocaleManager.getString("eventType"), 75));

        final TablePrintSettings tablePrintSettings = new TablePrintSettings("default");
        tablePrintSettings.setTableColumnWrappers(settings);
        return tablePrintSettings;
    }
}
