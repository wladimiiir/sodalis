
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core;

import sk.magiksoft.sodalis.core.logger.LoggerManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * @author wladimiiir
 */
public class EnvironmentSetup {
    private static final Properties ENVIRONMENT_PROPERTIES = new Properties();

    static {
        try {
            ENVIRONMENT_PROPERTIES.load(EnvironmentSetup.class.getResourceAsStream("environment.properties"));
        } catch (IOException e) {
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
        File file = new File("data/fonts");
        File[] fonts = file.listFiles();

        for (File fontFile : fonts) {
            if (fontFile.isDirectory()) {
                continue;
            }
            try {
                Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);

                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
                UIManager.put(fontFile.getName(), font);
            } catch (FontFormatException ex) {
                LoggerManager.getInstance().error(getClass(), ex);
            } catch (IOException ex) {
                LoggerManager.getInstance().error(getClass(), ex);
            }
        }
    }
}