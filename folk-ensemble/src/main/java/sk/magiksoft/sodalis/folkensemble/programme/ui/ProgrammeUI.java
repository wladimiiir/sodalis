package sk.magiksoft.sodalis.folkensemble.programme.ui;

import net.sf.jasperreports.engine.JRRewindableDataSource;
import sk.magiksoft.sodalis.category.CategoryDataManager;
import sk.magiksoft.sodalis.category.CategoryManager;
import sk.magiksoft.sodalis.category.report.CategoryWrapperDataSource;
import sk.magiksoft.sodalis.category.ui.CategoryTreeComboBox;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.action.ActionMessage;
import sk.magiksoft.sodalis.core.action.MessageAction;
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyJRDataSource;
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslatorManager;
import sk.magiksoft.sodalis.core.factory.IconFactory;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.printing.*;
import sk.magiksoft.sodalis.core.settings.TableSettings;
import sk.magiksoft.sodalis.core.ui.AbstractTableContext;
import sk.magiksoft.sodalis.core.utils.UIUtils;
import sk.magiksoft.sodalis.folkensemble.programme.ProgrammeContextManager;
import sk.magiksoft.sodalis.folkensemble.programme.ProgrammeModule;
import sk.magiksoft.sodalis.folkensemble.programme.action.AddProgrammeAction;
import sk.magiksoft.sodalis.folkensemble.programme.action.ProgrammeExportAction;
import sk.magiksoft.sodalis.folkensemble.programme.action.ProgrammeImportAction;
import sk.magiksoft.sodalis.folkensemble.programme.action.RemoveProgrammeAction;
import sk.magiksoft.sodalis.folkensemble.programme.entity.Programme;
import sk.magiksoft.sodalis.folkensemble.programme.report.ProgrammePrintDocument;
import sk.magiksoft.sodalis.folkensemble.programme.settings.ProgrammeSettings;
import sk.magiksoft.swing.HideableSplitPane;
import sk.magiksoft.swing.ISTable;
import sk.magiksoft.swing.MultiActionButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * @author wladimiiir
 */
public class ProgrammeUI extends AbstractTableContext implements PropertyChangeListener {

    private JToolBar toolBar;
    private JButton btnAdd;
    private JButton btnRemove;
    private MultiActionButton btnPrint;
    private JButton btnExport;
    private JButton btnImport;

    public ProgrammeUI() {
        super(Programme.class, new ISTable(new ProgrammeTableModel()));
        initComponents();
        SodalisApplication.get().getStorageManager().registerComponent("programmeUI", this);
        ProgrammeSettings.getInstance().addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ProgrammeSettings.O_SELECTED_CATEGORIES)) {
            List<Long> ids = (List<Long>) ProgrammeSettings.getInstance().getValue(
                    ProgrammeSettings.O_SELECTED_CATEGORIES);

            categoryTreeComboBox.setSelectedCategories(CategoryDataManager.getInstance().getCategories(ids));
        }
    }

    @Override
    protected void currentObjectChanged() {
        refreshButtons();
    }

    private void initToolBar() {
        final AbstractButton categoryTreeButton = categoryTreeComponent.getShowCategoryTreeButton();
        GridBagConstraints c = new GridBagConstraints();

        toolBar = UIUtils.createToolBar();
        toolBar.setLayout(new GridBagLayout());
        toolBar.setFloatable(false);

        btnAdd = new JButton(new AddProgrammeAction());
        btnRemove = new JButton(new RemoveProgrammeAction());
        btnPrint = new MultiActionButton(new Action[]{new PrintAction(), new ContextPrintAction(this, new ProgrammePrintDocument())},
                "", IconFactory.getInstance().getIcon("print"));
        btnExport = new JButton(new ProgrammeExportAction());
        btnImport = new JButton(new ProgrammeImportAction());

        initToolbarButton(btnAdd);
        initToolbarButton(btnRemove);
        initToolbarButton(btnPrint);
        initToolbarButton(btnExport);
        initToolbarButton(btnImport);
        initToolbarButton(categoryTreeButton);
        btnPrint.setEnabled(true);
        categoryTreeButton.setEnabled(true);

        categoryTreeComboBox = new CategoryTreeComboBox(ProgrammeModule.class);
        categoryTreeComboBox.addChangeListener(new CategoryTreeComboBoxChangeListener(ProgrammeSettings.getInstance(), ProgrammeContextManager.getInstance()));
        setupInputMap();

        c.gridx = c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 0.0;
        toolBar.add(btnAdd, c);
        c.gridx++;
        toolBar.add(btnRemove, c);
        c.gridx++;
        toolBar.add(btnPrint, c);
        c.gridx++;
        toolBar.add(btnExport, c);
        c.gridx++;
        toolBar.add(btnImport, c);
        c.gridx++;
        toolBar.add(categoryTreeButton, c);
        c.gridx++;
        c.weightx = 1.0;
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        toolBar.add(panel, c);
        c.gridx++;
        c.weightx = 0.0;
        toolBar.add(categoryTreeComboBox, c);

    }

    private void initComponents() {
        JScrollPane scrollPane = new JScrollPane(table);
        HideableSplitPane mainSplitPane = new HideableSplitPane(HideableSplitPane.VERTICAL_SPLIT, scrollPane,
                (controlPanel = new ProgrammeControlPanel()).getControlComponent());

        table.setName("programmeUI.table");
        initCategoryTreeComponent(ProgrammeModule.class, scrollPane);
        initToolBar();
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setLayout(new BorderLayout());
        add(mainSplitPane, BorderLayout.CENTER);
        add(toolBar, BorderLayout.NORTH);
        if (ProgrammeContextManager.getInstance().getFilterPanel() != null) {
            add(ProgrammeContextManager.getInstance().getFilterPanel(), BorderLayout.EAST);
        }
        controlPanel.getControlComponent().setMinimumSize(new Dimension(200, 340));
        mainSplitPane.setName("programmeUI.splitPane");
        mainSplitPane.setLeftText(LocaleManager.getString("programmeList"));
        mainSplitPane.setRightText(LocaleManager.getString("details"));
        mainSplitPane.setDividerLocation((int) 300);
        refreshButtons();
        TableSettings.getInstance().registerTable("PROGRAMME_TABLE", table);
    }

    private void setupInputMap() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(
                KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK).toString());
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(
                KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK).toString());
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(
                KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK).toString());
        actionMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK).toString(), new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btnAdd.doClick();
            }
        });
        actionMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK).toString(), new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btnRemove.doClick();
            }
        });
        actionMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK).toString(), new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btnPrint.doClick();
            }
        });
    }

    private void refreshButtons() {
        ActionMessage actionMessage;

        actionMessage = ((MessageAction) btnAdd.getAction()).getActionMessage(getSelectedEntities());
        btnAdd.setEnabled(actionMessage.isAllowed());
        btnAdd.setToolTipText(actionMessage.getMessage());
        actionMessage = ((MessageAction) btnRemove.getAction()).getActionMessage(getSelectedEntities());
        btnRemove.setEnabled(actionMessage.isAllowed());
        btnRemove.setToolTipText(actionMessage.getMessage());
        actionMessage = ((MessageAction) btnExport.getAction()).getActionMessage(getSelectedEntities());
        btnExport.setEnabled(actionMessage.isAllowed());
        btnExport.setToolTipText(actionMessage.getMessage());
        actionMessage = ((MessageAction) btnImport.getAction()).getActionMessage(getSelectedEntities());
        btnImport.setEnabled(actionMessage.isAllowed());
        btnImport.setToolTipText(actionMessage.getMessage());
    }

    private class PrintAction extends MessageAction {

        public PrintAction() {
            super(LocaleManager.getString("tablePrint"));
        }

        @Override
        public ActionMessage getActionMessage(List objects) {
            return new ActionMessage(true, LocaleManager.getString("print"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean categoryShown = categoryTreeComponent.isComponentShown();
            final List objects = categoryShown
                    ? CategoryManager.getInstance().getCategoryPathWrappers(categoryTreeComponent.getRoot())
                    : getEntities();
            JRRewindableDataSource dataSource = new EntityPropertyJRDataSource<Programme>(scala.collection.JavaConversions.asScalaBuffer(objects).toList());
            if (categoryShown) {
                dataSource = new CategoryWrapperDataSource(objects, (JRExtendedDataSource) dataSource);
            }

            TablePrintDialog dialog = new TablePrintDialog(
                    (TablePrintSettings) ProgrammeSettings.getInstance().getValue(
                            ProgrammeSettings.O_DEFAULT_PRINT_SETTINGS),
                    EntityPropertyTranslatorManager.getTranslator(Programme.class),
                    (List<TablePrintSettings>) ProgrammeSettings.getInstance().getValue(
                            ProgrammeSettings.O_USER_PRINT_SETTINGS),
                    dataSource);

            if (categoryShown) {
                dialog.setGroups(categoryTreeComponent.getSelectedCategoryPath());
            }

            dialog.addTableSettingsListener(new TableSettingsListener() {

                @Override
                public void tableSettingsSaved(TablePrintSettings settings) {
                    final List<TablePrintSettings> userSettings =
                            (List<TablePrintSettings>) ProgrammeSettings.getInstance().getValue(
                                    ProgrammeSettings.O_USER_PRINT_SETTINGS);

                    for (TablePrintSettings tablePrintSettings : userSettings) {
                        if (tablePrintSettings.getName().equals(settings.getName())) {
                            userSettings.remove(tablePrintSettings);
                            break;
                        }
                    }
                    userSettings.add(settings);
                    ProgrammeSettings.getInstance().save();
                }

                @Override
                public void tableSettingsDeleted(TablePrintSettings settings) {
                    final List<TablePrintSettings> userSettings =
                            (List<TablePrintSettings>) ProgrammeSettings.getInstance().getValue(
                                    ProgrammeSettings.O_USER_PRINT_SETTINGS);

                    for (TablePrintSettings tablePrintSettings : userSettings) {
                        if (tablePrintSettings.getName().equals(settings.getName())) {
                            userSettings.remove(tablePrintSettings);
                            break;
                        }
                    }
                    ProgrammeSettings.getInstance().save();
                }
            });

            dialog.setVisible(true);
        }
    }
}
