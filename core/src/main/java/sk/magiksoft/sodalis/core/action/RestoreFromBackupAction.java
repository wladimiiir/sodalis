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
public class RestoreFromBackupAction extends AbstractAction {

    private JFileChooser fileChooser;

    public RestoreFromBackupAction() {
        super(LocaleManager.getString("restoreFromBackup"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter(LocaleManager.getString("backupFiles"), "backup"));
            fileChooser.setMultiSelectionEnabled(false);
        }

        if (fileChooser.showOpenDialog(SodalisApplication.get().getMainFrame()) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File backupFile = fileChooser.getSelectedFile();
        if (backupFile == null) {
            return;
        }

        BackupManager.getInstance().restore(backupFile);

    }

}
