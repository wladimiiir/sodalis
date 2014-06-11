
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

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import sk.magiksoft.backup.BackupManager;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.locale.LocaleManager;

/**
 *
 * @author wladimiiir
 */
public class RestoreFromBackupAction extends AbstractAction{

    private JFileChooser fileChooser;

    public RestoreFromBackupAction() {
        super(LocaleManager.getString("restoreFromBackup"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(fileChooser==null){
            fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter(LocaleManager.getString("backupFiles"), "backup"));
            fileChooser.setMultiSelectionEnabled(false);
        }

        if(fileChooser.showOpenDialog(SodalisApplication.get().getMainFrame())!=JFileChooser.APPROVE_OPTION){
            return;
        }
        File backupFile = fileChooser.getSelectedFile();
        if(backupFile==null){
            return;
        }

        BackupManager.getInstance().restore(backupFile);
       
    }

}