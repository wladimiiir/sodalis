package sk.magiksoft.sodalis.app;

import org.jdesktop.application.Application;
import sk.magiksoft.sodalis.core.ClassPathLoaderInjectors;
import sk.magiksoft.sodalis.core.EnvironmentSetup;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.SodalisManager;

import java.io.File;

/**
 * @author wladimiiir
 */
public class Launcher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ClassPathLoaderInjectors.injectLibraryDir(new File(SodalisManager.LIBRARIES_DIRECTORY()));

        new EnvironmentSetup().setup();

        Application.launch(SodalisApplication.class, new String[0]);
    }
}
