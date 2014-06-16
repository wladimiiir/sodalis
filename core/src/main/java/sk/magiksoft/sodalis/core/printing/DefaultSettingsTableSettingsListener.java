
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/


package sk.magiksoft.sodalis.core.printing;

import sk.magiksoft.sodalis.core.settings.Settings;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 10/26/10
 * Time: 9:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultSettingsTableSettingsListener implements TableSettingsListener {

    private Settings settings;

    public DefaultSettingsTableSettingsListener(Settings settings) {
        this.settings = settings;
    }

    @Override
    public void tableSettingsDeleted(TablePrintSettings settings) {
        final List<TablePrintSettings> userSettings = (List<TablePrintSettings>) this.settings.getValue(Settings.O_USER_PRINT_SETTINGS);

        for (TablePrintSettings tablePrintSettings : userSettings) {
            if (tablePrintSettings.getName().equals(settings.getName())) {
                userSettings.remove(tablePrintSettings);
                break;
            }
        }
        this.settings.save();
    }

    @Override
    public void tableSettingsSaved(TablePrintSettings settings) {
        List<TablePrintSettings> userSettings = (List<TablePrintSettings>) this.settings.getValue(Settings.O_USER_PRINT_SETTINGS);

        if (userSettings == null) {
            userSettings = new LinkedList<TablePrintSettings>();
            this.settings.setValue(Settings.O_USER_PRINT_SETTINGS, userSettings);
        }
        for (TablePrintSettings tablePrintSettings : userSettings) {
            if (tablePrintSettings.getName().equals(settings.getName())) {
                userSettings.remove(tablePrintSettings);
                break;
            }
        }
        userSettings.add(settings);
        this.settings.save();
    }
}