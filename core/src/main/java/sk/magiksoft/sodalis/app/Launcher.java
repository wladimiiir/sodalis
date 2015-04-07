package sk.magiksoft.sodalis.app;

import org.jdesktop.application.Application;
import sk.magiksoft.sodalis.core.ClassPathLoaderInjector;
import sk.magiksoft.sodalis.core.Constants;
import sk.magiksoft.sodalis.core.EnvironmentSetup;
import sk.magiksoft.sodalis.core.SodalisApplication;

import java.io.File;

/**
 * @author wladimiiir
 */
public class Launcher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (System.getProperty("dynamicModuleManager", "false").equalsIgnoreCase("false")) {
            ClassPathLoaderInjector.injectLibraryDir(new File(Constants.LIBRARIES_DIRECTORY));
        }

        new EnvironmentSetup().setup();

        Application.launch(SodalisApplication.class, new String[0]);
    }
}
