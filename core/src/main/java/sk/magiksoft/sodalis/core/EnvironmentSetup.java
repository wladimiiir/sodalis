package sk.magiksoft.sodalis.core;

import sk.magiksoft.sodalis.core.logger.LoggerManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author wladimiiir
 */
public class EnvironmentSetup {
    private static final Properties ENVIRONMENT_PROPERTIES = new Properties();

    static {
        try {
            final Locale locale = Locale.getDefault();
            InputStream environmentProperties = EnvironmentSetup.class.getResourceAsStream("environment_" + locale.toString() + ".properties");
            if (environmentProperties == null) {
                environmentProperties = EnvironmentSetup.class.getResourceAsStream("environment.properties");
            }
            if (environmentProperties != null) {
                ENVIRONMENT_PROPERTIES.load(environmentProperties);
            }
        } catch (IOException e) {
            //ignore
        }
    }

    public void setup() {
        setupLAF();

        for (Object key : ENVIRONMENT_PROPERTIES.keySet()) {
            UIManager.put(key, ENVIRONMENT_PROPERTIES.getProperty((String) key));
        }
        setupFonts();
    }

    private void setupLAF() {
//        try {
//            Skin skin = SkinLookAndFeel.loadThemePack("solunaRthemepack.zip");
//            SkinLookAndFeel.setSkin(skin);
//        } catch (Exception e) {
//            LoggerManager.getInstance().error(getClass(), e);
//        }

    }

    private void setupFonts() {
        final Scanner scanner = new Scanner(getClass().getResourceAsStream("font"));

        while (scanner.hasNextLine()) {
            final String fontName = scanner.nextLine();
            try {
                Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("font/" + fontName));

                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
                UIManager.put(fontName, font);
            } catch (FontFormatException | IOException ex) {
                LoggerManager.getInstance().error(getClass(), ex);
            }
        }

    }
}
