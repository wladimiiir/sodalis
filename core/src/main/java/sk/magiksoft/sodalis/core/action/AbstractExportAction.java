package sk.magiksoft.sodalis.core.action;

import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.factory.IconFactory;
import sk.magiksoft.sodalis.core.imex.ImExManager;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;
import sk.magiksoft.swing.ISFileChooser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

/**
 * @author wladimiiir
 * @since 2010/7/18
 */
public abstract class AbstractExportAction extends MessageAction {
    public static final int EXPORT_TYPE_ALL = 1;
    public static final int EXPORT_TYPE_SELECTED = 2;
    protected JFileChooser fileChooser;

    public AbstractExportAction() {
        super("", IconFactory.getInstance().getIcon("export"));
        initFileChooser();
    }

    private void initFileChooser() {
        fileChooser = new ISFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(LocaleManager.getString("XMLFiles"), "xml"));
    }

    protected File getExportFile() {
        int result = fileChooser.showSaveDialog(SodalisApplication.get().getMainFrame());

        if (result != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File exportFile = fileChooser.getSelectedFile();
        if (!exportFile.getName().endsWith(".xml")) {
            exportFile = new File(exportFile.getAbsolutePath() + ".xml");
        }
        return exportFile;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File exportFile;
        List exportItems;
        int exportType = getExportType();

        if (exportType == -1) {
            return;
        }
        exportFile = getExportFile();
        if (exportFile == null) {
            return;
        }

        exportItems = getExportItems(exportType);

        try {
            ImExManager.exportData(exportFile, exportItems);
            SodalisApplication.get().showMessage(LocaleManager.getString("exportDone"));
        } catch (Exception ex) {
            LoggerManager.getInstance().error(getClass(), ex);
            SodalisApplication.get().showMessage(LocaleManager.getString("exportFailed"));
        }
    }

    protected abstract List<? extends Object> getExportItems(int exportType);

    protected int getExportType() {
        JRadioButton rbtAll = new JRadioButton(LocaleManager.getString("all"), true);
        JRadioButton rbtSelected = new JRadioButton(LocaleManager.getString("selected"));
        ButtonGroup btnGroup = new ButtonGroup();

        btnGroup.add(rbtAll);
        btnGroup.add(rbtSelected);

        int result = ISOptionPane.showConfirmDialog(SodalisApplication.get().getMainFrame(),
                new Object[]{rbtAll, rbtSelected},
                LocaleManager.getString("export"), ISOptionPane.OK_CANCEL_OPTION);

        if (result != ISOptionPane.OK_OPTION) {
            return -1;
        }

        if (rbtAll.isSelected()) {
            return EXPORT_TYPE_ALL;
        }
        if (rbtSelected.isSelected()) {
            return EXPORT_TYPE_SELECTED;
        }

        return -1;
    }


}
