
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.backup;

import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.data.DefaultDataManager;
import sk.magiksoft.sodalis.core.data.SchemaCreator;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;
import sk.magiksoft.swing.ProgressDialog;

import java.io.File;

/**
 * @author wladimiiir
 */
public class BackupManager {

    private static BackupManager instance = null;

    private BackupManager() {
        instance = this;
    }

    public static synchronized BackupManager getInstance() {
        if (instance == null) {
            new BackupManager();
        }

        return instance;
    }

    public void restore(final File backupFile) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                boolean result = false;
                ProgressDialog dialog = new ProgressDialog();

                dialog.setProgressMessage(LocaleManager.getString("restoringDatabase"));
                dialog.startProgress();

                try {
                    DefaultDataManager.getInstance().closeSessionFactory();
                    result = SodalisApplication.getDBManager().restore(backupFile);
                    if (result) {
                        SchemaCreator.updateDBSchema();
                    }
                } catch (Exception ex) {
                    LoggerManager.getInstance().error(getClass(), ex);
                } finally {
                    dialog.stopProgress();
                }

                if (result) {
                    ISOptionPane.showMessageDialog(SodalisApplication.get().getMainFrame(), LocaleManager.getString("restoreDBSuccessful"));
                    SodalisApplication.get().restartApplication();
                } else {
                    ISOptionPane.showMessageDialog(SodalisApplication.get().getMainFrame(), LocaleManager.getString("restoreDBFailed"));
                }
            }
        }).start();
    }

    public void doBackup(final File backupFile) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                boolean result = false;
                ProgressDialog dialog = new ProgressDialog();

                dialog.setProgressMessage(LocaleManager.getString("backingUp"));
                dialog.startProgress();

                try {
                    result = SodalisApplication.getDBManager().doBackup(backupFile);
                } catch (Exception ex) {
                    LoggerManager.getInstance().error(getClass(), ex);
                } finally {
                    dialog.stopProgress();
                }

                if (result) {
                    ISOptionPane.showMessageDialog(SodalisApplication.get().getMainFrame(), LocaleManager.getString("backupSuccessful"));
                } else {
                    ISOptionPane.showMessageDialog(SodalisApplication.get().getMainFrame(), LocaleManager.getString("backupFailed"));
                }
            }
        }).start();
    }
}