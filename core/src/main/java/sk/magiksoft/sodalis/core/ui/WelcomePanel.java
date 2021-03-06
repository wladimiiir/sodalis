package sk.magiksoft.sodalis.core.ui;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

/**
 * @author wladimiiir
 */
public class WelcomePanel extends SlideshowPanel {
    private static File welcomePageFile;

    static {
        welcomePageFile = new File("data/welcome_page/welcome_" + Locale.getDefault().toString() + ".html");
        if (!welcomePageFile.exists()) {
            welcomePageFile = new File("data/welcome_page/welcome.html");
        }
    }

    public WelcomePanel() throws MalformedURLException {
        super(new URL[]{
                welcomePageFile.toURI().toURL()
        });
    }
}
