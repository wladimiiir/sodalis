package sk.magiksoft.sodalis.icon;

import sk.magiksoft.sodalis.core.logger.LoggerManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * @author wladimiiir
 */
public class IconManager {
    private static IconManager instance = null;

    private final Properties iconProperties = new Properties();

    private IconManager() {
        registerIcons(getClass().getResource("icons.properties"));
    }

    public static IconManager getInstance() {
        if (instance == null) {
            instance = new IconManager();
        }
        return instance;
    }

    public void registerIcons(URL propertyURL) {
        InputStream inputStream;
        try {
            inputStream = propertyURL.openStream();
            iconProperties.load(inputStream);
            inputStream.close();
        } catch (IOException ex) {
            LoggerManager.getInstance().error(IconManager.class, ex);
        }
    }

    public Icon getIcon(String iconName) {
        return iconProperties.containsKey(iconName) ? new ImageIcon(getClass().getResource(iconProperties.getProperty(iconName))) : null;
    }

    public Icon getIcon(String iconName, final int size) {
        return iconProperties.getProperty(iconName) != null ? new ImageIcon(iconProperties.getProperty(iconName)) {

            @Override
            public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
                g.drawImage(getImage(), 0, 0, size, size, getImageObserver());
            }

            @Override
            public int getIconHeight() {
                return size;
            }

            @Override
            public int getIconWidth() {
                return size;
            }

        } : null;
    }
}
