
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.security;

import java.util.HashMap;
import java.util.Map;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;

/**
 *
 * @author wladimiiir
 */
public class SodalisConfiguration extends Configuration{

    private static SodalisConfiguration configuration;

    private SodalisConfiguration() {
    }

    public static void initConfiguration(){
        configuration = new SodalisConfiguration();
        Configuration.setConfiguration(configuration);
    }

    public static SodalisConfiguration getConfiguration() {
        return configuration;
    }


    @Override
    public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
        final AppConfigurationEntry[] appConfigurationEntrys = new AppConfigurationEntry[]{
            new AppConfigurationEntry(
                    SodalisLoginModule.class.getName(),
                    AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
                    new HashMap<String, Object>()
            )
        };

        return appConfigurationEntrys;
    }

}