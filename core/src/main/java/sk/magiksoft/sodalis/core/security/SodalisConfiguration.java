package sk.magiksoft.sodalis.core.security;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import java.util.HashMap;

/**
 * @author wladimiiir
 */
public class SodalisConfiguration extends Configuration {

    private static SodalisConfiguration configuration;

    private SodalisConfiguration() {
    }

    public static void initConfiguration() {
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
