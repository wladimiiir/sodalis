
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.action;

import sk.magiksoft.backup.BackupManager;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.locale.LocaleManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * @author wladimiiir
 */
public class BackupAction extends AbstractAction {

    private JFileChooser fileChooser;

    public BackupAction() {
        super(LocaleManager.getString("backup"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter(LocaleManager.getString("backupFiles"), "backup", "sql"));
            fileChooser.setMultiSelectionEnabled(false);
        }

        if (fileChooser.showSaveDialog(SodalisApplication.get().getMainFrame()) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File backupFile = fileChooser.getSelectedFile();
        if (backupFile == null) {
            return;
        }
        if (!backupFile.getName().endsWith(".backup")) {
            backupFile = new File(backupFile.getAbsolutePath() + ".backup");
        }
        BackupManager.getInstance().doBackup(backupFile);
    }

}