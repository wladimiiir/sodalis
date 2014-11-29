package sk.magiksoft.sodalis.app;

import org.jdesktop.application.Application;
import sk.magiksoft.sodalis.core.EnvironmentSetup;
import sk.magiksoft.sodalis.core.SodalisApplication;

/**
 * @author wladimiiir
 */
public class Launcher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new EnvironmentSetup().setup();

        Application.launch(SodalisApplication.class, new String[0]);
    }
}
