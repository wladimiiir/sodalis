
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.swing;

import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.utils.UIUtils;

import javax.accessibility.AccessibleContext;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;

/**
 * @author wladimiiir
 */
public class ISFileChooser extends JFileChooser {

    public ISFileChooser(String currentDirectoryPath, FileSystemView fsv) {
        super(currentDirectoryPath, fsv);
    }

    public ISFileChooser(File currentDirectory, FileSystemView fsv) {
        super(currentDirectory, fsv);
    }

    public ISFileChooser(FileSystemView fsv) {
        super(fsv);
    }

    public ISFileChooser(File currentDirectory) {
        super(currentDirectory);
    }

    public ISFileChooser(String currentDirectoryPath) {
        super(currentDirectoryPath);
    }

    public ISFileChooser() {
    }

    @Override
    protected JDialog createDialog(Component parent) throws HeadlessException {
        putClientProperty(AccessibleContext.ACCESSIBLE_DESCRIPTION_PROPERTY,
                getUI().getDialogTitle(this));

        JDialog dialog = new JDialog(SodalisApplication.get().getMainFrame(), getUI().getDialogTitle(this), true);
        dialog.setComponentOrientation(this.getComponentOrientation());

        Container contentPane = dialog.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(this, BorderLayout.CENTER);

        if (JDialog.isDefaultLookAndFeelDecorated()) {
            boolean supportsWindowDecorations =
                    UIManager.getLookAndFeel().getSupportsWindowDecorations();
            if (supportsWindowDecorations) {
                dialog.getRootPane().setWindowDecorationStyle(JRootPane.FILE_CHOOSER_DIALOG);
            }
        }

        UIUtils.makeISDialog(dialog);

        dialog.pack();
        dialog.setLocationRelativeTo(parent);

        return dialog;
    }
}