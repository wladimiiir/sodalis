
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.settings;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.data.remote.client.ClientDataManager;
import sk.magiksoft.sodalis.core.exception.VetoException;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.security.LoginManagerService;
import sk.magiksoft.sodalis.core.security.event.LoginAdapter;
import sk.magiksoft.sodalis.core.security.event.LoginEvent;
import sk.magiksoft.sodalis.core.utils.UIUtils;

import javax.security.auth.Subject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class SettingsManager extends ClientDataManager {

    private static final File SETTINGS_FILE = new File("./config/sodalis.xml");
    private static SettingsManager instance = null;
    private SettingsDialog settingsDialog;

    private SettingsManager() {
        instance = this;
        SodalisApplication.get().getService(LoginManagerService.class, LoginManagerService.SERVICE_NAME).addLoginListener(new LoginAdapter() {

            @Override
            public void subjectLoggedOut(LoginEvent event) {
                settingsDialog = null;
            }
        });
    }

    public static synchronized SettingsManager getInstance() {
        if (instance == null) {
            new SettingsManager();
        }

        return instance;
    }

    private void initSettingsDialog() {
        Subject subject = SodalisApplication.get().getService(LoginManagerService.class, LoginManagerService.SERVICE_NAME).getLoggedSubject();

        settingsDialog = new SettingsDialog(SodalisApplication.get().getMainFrame());
        settingsDialog.setTitle(LocaleManager.getString("settings"));
        UIUtils.makeISDialog(settingsDialog);
        settingsDialog.setSize(800, 600);
        settingsDialog.setLocationRelativeTo(null);

        for (SettingsPanel settingsPanel : getSettingsPanels()) {
            try {
                settingsPanel.setup(subject);
            } catch (VetoException e) {
                continue;
            }
            settingsDialog.addSettingsPanel(settingsPanel);
        }

    }

    private List<SettingsPanel> getSettingsPanels() {
        List<SettingsPanel> settingsPanels = new ArrayList<SettingsPanel>();
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = builder.build(SETTINGS_FILE);
            Element root = document.getRootElement();
            root = root.getChild("settings");
            List<Element> moduleElements = root.getChildren("settings_panel");
            List<Element> classElements;
            Class settingsClass;
            String className;
            SettingsPanel settingsPanel;
            CompoundSettingsPanel compoundSettingsPanel;

            for (Element moduleElement : moduleElements) {
                classElements = moduleElement.getChildren("class");
                if (classElements.size() > 1) {
                    compoundSettingsPanel = new CompoundSettingsPanel();
                    compoundSettingsPanel.setSettingsPanelName(moduleElement.getAttributeValue("name"));
                    for (Element classElement : classElements) {
                        className = classElement.getTextTrim();
                        settingsClass = Class.forName(className);
                        if (SettingsPanel.class.isAssignableFrom(settingsClass)) {
                            //Settings panel got from xml
                            settingsPanel = (SettingsPanel) settingsClass.newInstance();
                        } else {
                            //default settings panel based on Settings
                            settingsPanel = new DefaultSettingsPanel(LocaleManager.getString("generalSettings"),
                                    Integer.valueOf(classElement.getAttributeValue("columns")), settingsClass);
                        }
                        compoundSettingsPanel.addSettingsPanel(settingsPanel);
                    }
                    settingsPanels.add(compoundSettingsPanel);
                } else {
                    className = classElements.get(0).getTextTrim();
                    settingsClass = Class.forName(className);
                    if (SettingsPanel.class.isAssignableFrom(settingsClass)) {
                        //Settings panel got from xml
                        settingsPanel = (SettingsPanel) settingsClass.newInstance();
                    } else {
                        //default settings panel based on Settings
                        settingsPanel = new DefaultSettingsPanel(moduleElement.getAttributeValue("name"),
                                Integer.valueOf(classElements.get(0).getAttributeValue("columns")), settingsClass);
                    }
                    settingsPanels.add(settingsPanel);
                }
            }
        } catch (IllegalAccessException ex) {
            LoggerManager.getInstance().error(SettingsManager.class, ex);
        } catch (InstantiationException ex) {
            LoggerManager.getInstance().error(SettingsManager.class, ex);
        } catch (ClassNotFoundException ex) {
            LoggerManager.getInstance().error(SettingsManager.class, ex);
        } catch (IOException ex) {
            LoggerManager.getInstance().error(SettingsManager.class, ex);
        } catch (JDOMException ex) {
            LoggerManager.getInstance().error(SettingsManager.class, ex);
        }


        return settingsPanels;
    }

    public void showSettingsDialog() {
        if (settingsDialog == null) {
            initSettingsDialog();
        }
        settingsDialog.reloadSettings();
        settingsDialog.setVisible(true);
    }
}