
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.factory;

import java.awt.Component;
import java.awt.Graphics;
import java.io.IOException;
import java.util.Properties;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import sk.magiksoft.sodalis.core.logger.LoggerManager;

/**
 *
 * @author wladimiiir
 */
public class IconFactory {
    private static IconFactory instance=null;
    
    private Properties iconProperties;
    
    public IconFactory() {
        try {
            instance = this;
            iconProperties = new Properties();
            iconProperties.load(IconFactory.class.getResourceAsStream("icons.properties"));
        } catch (IOException ex) {
            LoggerManager.getInstance().error(IconFactory.class, ex);
        }
    }
    
    
    public static IconFactory getInstance() {
        if(instance==null){
            new IconFactory();
        }
        return instance;
    }

    public Icon getIcon(String iconName){
        return iconProperties.getProperty(iconName)!=null ? new ImageIcon(iconProperties.getProperty(iconName)) : null;
    }

    public Icon getIcon(String iconName, final int size){
        return iconProperties.getProperty(iconName)!=null ? new ImageIcon(iconProperties.getProperty(iconName)){

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