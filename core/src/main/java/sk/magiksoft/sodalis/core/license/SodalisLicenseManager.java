
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.license;

import java.text.MessageFormat;
import java.util.List;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.data.DefaultDataManager;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;

/**
 *
 * @author wladimiiir
 */
public class SodalisLicenseManager extends LicenseManager {

    public SodalisLicenseManager() throws LicenseException {
        super();
        verifyFiles();
        checkSubscription();
    }

    private void checkSubscription() throws LicenseException {
        Integer dayCount = (Integer) getLicense().getProperty(License.PROPERTY_SUBSCRIBED_DAY_COUNT);
        long currentTime = System.currentTimeMillis();
        long creationTime;
        int daysLeft;
        List times;

        if (dayCount == null) {
            return;
        }

        times = DefaultDataManager.getInstance().getSQLQueryList("SELECT min(creationTime) FROM settings");
        if (times.isEmpty() || times.get(0) == null) {
            creationTime = currentTime;
        } else {
            creationTime = Long.valueOf(times.get(0).toString());
        }
        if (creationTime > currentTime) {
            throw new LicenseException(LocaleManager.getString("license.expired"));
        }
        daysLeft = dayCount - (int) ((currentTime - creationTime) / (1000 * 60 * 60 * 24));
        if (daysLeft <= 0) {
            throw new LicenseException(LocaleManager.getString("license.expired"));
        }

        ISOptionPane.showMessageDialog(SodalisApplication.get().getMainFrame(),
                MessageFormat.format(LocaleManager.getString("license.daysLeft"), daysLeft));
    }
}