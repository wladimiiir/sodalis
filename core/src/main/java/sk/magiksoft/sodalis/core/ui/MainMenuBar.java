
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.ui;

import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.action.*;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.settings.SettingsManager;
import sk.magiksoft.swing.gradient.GradientMenuBar;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 *
 * @author wladimiiir
 */
public class MainMenuBar extends GradientMenuBar {

    public MainMenuBar() {
        initMenuBar();
    }

    private void initMenuBar() {
        JMenu mainMenu = new JMenu("Sodalis");

        mainMenu.add(new JMenuItem(new AbstractAction(LocaleManager.getString("settings")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsManager.getInstance().showSettingsDialog();
            }
        }));
        mainMenu.addSeparator();
        mainMenu.add(new UpdateAction());
        mainMenu.add(new UpdateFromFileAction());
        mainMenu.add(new BackupAction());
        mainMenu.add(new RestoreFromBackupAction());
        mainMenu.addSeparator();
        mainMenu.add(new AbstractAction(LocaleManager.getString("versionInfo")) {
            private VersionDialog dialog;

            @Override public void actionPerformed(ActionEvent e) {
                if (dialog == null) {
                    dialog = new VersionDialog();
                }
                dialog.setVisible(true);
            }
        });
        mainMenu.add(new ShowWelcomePanelAction());
        mainMenu.add(new LogoutAction());
        mainMenu.add(new AbstractAction(LocaleManager.getString("restart")) {
            @Override public void actionPerformed(ActionEvent e) {
                SodalisApplication.get().restartApplication();
            }
        });
        mainMenu.add(new AbstractAction(LocaleManager.getString("Exit")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                SodalisApplication.get().exit();
            }
        });

        add(mainMenu);
    }

    private class ShowWelcomePanelAction extends AbstractAction{

        public ShowWelcomePanelAction() {
            super(LocaleManager.getString("WelcomePage"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SodalisApplication.get().showWelcomePage();
        }
    }
}