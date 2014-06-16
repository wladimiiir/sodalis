
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/


package sk.magiksoft.sodalis.core.action;

import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.factory.IconFactory;
import sk.magiksoft.sodalis.core.function.Function;
import sk.magiksoft.sodalis.core.imex.ImExManager;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.swing.ISFileChooser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.MessageFormat;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jul 18, 2010
 * Time: 7:36:09 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractImportAction extends MessageAction {
    private JFileChooser fileChooser;

    public AbstractImportAction() {
        super("", IconFactory.getInstance().getIcon("import"));
    }

    protected void initFileChooser(JFileChooser fileChooser) {
        fileChooser.setFileFilter(new FileNameExtensionFilter(LocaleManager.getString("XMLFiles"), "xml"));
    }

    protected List importFile(File file) {
        return ImExManager.importFile(file);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (fileChooser == null) {
            fileChooser = new ISFileChooser();
            initFileChooser(fileChooser);
        }

        final File importFile;
        int result = fileChooser.showOpenDialog(SodalisApplication.get().getMainFrame());

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        importFile = fileChooser.getSelectedFile();

        final Function importFunction = new Function() {
            @Override
            public void apply() {
                List importedObjects = importFile(importFile);
                if (importedObjects == null) {
                    SodalisApplication.get().showError(LocaleManager.getString("wrongImportFile"), importFile.getName());
                } else {
                    importObjects(importedObjects);
                }
            }
        };
        SodalisApplication.get().runProgress(LocaleManager.getString("importing"),
                importFunction, MessageFormat.format(LocaleManager.getString("wrongImportFile"), importFile.getName()));
    }

    protected abstract void importObjects(List objects);
}