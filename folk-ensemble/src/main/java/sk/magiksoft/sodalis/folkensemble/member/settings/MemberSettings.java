package sk.magiksoft.sodalis.folkensemble.member.settings;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.printing.TableColumnWrapper;
import sk.magiksoft.sodalis.core.printing.TablePrintSettings;
import sk.magiksoft.sodalis.core.settings.Global;
import sk.magiksoft.sodalis.core.settings.Settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wladimiiir
 */
public class MemberSettings extends Settings {

    @Global
    public static final String I_MEMBER_ID_NUMBER = "memberIDNumber";
    @Global
    public static final String S_MEMBER_ID_PREFIX = "memberIDPrefix";
    public static final String S_UNIVERSITY_HISTORY = "universityHistory";
    public static final String S_FACULTY_HISTORY = "facultyHistory";
    public static final String S_DEPARTMENT_HISTORY = "departmentHistory";
    public static final String S_SPECIALIZATION_HISTORY = "specializationHistory";


    private static MemberSettings instance = null;

    private MemberSettings() {
        super(MemberSettings.class.getName());
    }

    public static synchronized MemberSettings getInstance() {
        if (instance == null) {
            instance = new MemberSettings();
        }
        return instance;
    }

    @Override
    protected Map<String, Object> getDefaultSettingsMap() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put(I_MEMBER_ID_NUMBER, 0);
        map.put(S_MEMBER_ID_PREFIX, "FS");
        map.put(S_UNIVERSITY_HISTORY, "");
        map.put(S_SPECIALIZATION_HISTORY, "");
        map.put(S_FACULTY_HISTORY, "");
        map.put(S_DEPARTMENT_HISTORY, "");
        map.put(O_DEFAULT_PRINT_SETTINGS, getDefaultPrintSettings());
        map.put(O_USER_PRINT_SETTINGS, new ArrayList());
        map.put(O_SELECTED_CATEGORIES, new ArrayList());
        return map;
    }

    private Object getDefaultPrintSettings() {
        TablePrintSettings printSettings;
        List<TableColumnWrapper> tableColumns = new ArrayList<TableColumnWrapper>();
        TableColumnWrapper tableColumn;

        tableColumn = new TableColumnWrapper("fullName", LocaleManager.getString("lastNameFirstName"), 75);
        tableColumns.add(tableColumn);
        tableColumn = new TableColumnWrapper("birthDate", LocaleManager.getString("birthDate"), 75, TableColumnWrapper.Alignment.RIGHT, false);
        tableColumns.add(tableColumn);
        tableColumn = new TableColumnWrapper("address", LocaleManager.getString("address"), 75);
        tableColumns.add(tableColumn);
        tableColumn = new TableColumnWrapper("ensembleGroup", LocaleManager.getString("EnsembleGroup.name"), 75);
        tableColumns.add(tableColumn);
        tableColumn = new TableColumnWrapper("signature", LocaleManager.getString("signature"), 75);
        tableColumns.add(tableColumn);

        printSettings = new TablePrintSettings("");
        printSettings.setTableColumnWrappers(tableColumns);
        printSettings.setShowPageNumbers(true);
        return printSettings;
    }
}
