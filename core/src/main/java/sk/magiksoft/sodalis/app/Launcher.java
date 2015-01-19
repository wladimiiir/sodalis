package sk.magiksoft.sodalis.app;

import org.jdesktop.application.Application;
import sk.magiksoft.sodalis.core.*;
import sk.magiksoft.sodalis.core.module.OverridesModule;

import java.io.File;

/**
 * @author wladimiiir
 */
public class Launcher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ClassPathLoaderInjector.injectLibraryDir(new File(Constants.LIBRARIES_DIRECTORY));

        new EnvironmentSetup().setup();

        Application.launch(SodalisApplication.class, new String[0]);
    }
}
