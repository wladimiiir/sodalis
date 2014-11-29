package sk.magiksoft.sodalis.folkensemble.repertory.ui;

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
import sk.magiksoft.sodalis.core.printing.JRExtendedDataSource;
import sk.magiksoft.sodalis.core.printing.TablePrintDialog;
import sk.magiksoft.sodalis.core.printing.TablePrintSettings;
import sk.magiksoft.sodalis.core.printing.TableSettingsListener;
import sk.magiksoft.sodalis.core.settings.TableSettings;
import sk.magiksoft.sodalis.core.ui.AbstractTableContext;
import sk.magiksoft.sodalis.core.utils.UIUtils;
import sk.magiksoft.sodalis.folkensemble.repertory.RepertoryContextManager;
import sk.magiksoft.sodalis.folkensemble.repertory.RepertoryModule;
import sk.magiksoft.sodalis.folkensemble.repertory.action.AddSongAction;
import sk.magiksoft.sodalis.folkensemble.repertory.action.RemoveSongAction;
import sk.magiksoft.sodalis.folkensemble.repertory.action.SongExportAction;
import sk.magiksoft.sodalis.folkensemble.repertory.action.SongImportAction;
import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song;
import sk.magiksoft.sodalis.folkensemble.repertory.settings.RepertorySettings;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.swing.HideableSplitPane;
import sk.magiksoft.swing.ISTable;

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
public class RepertoryUI extends AbstractTableContext implements PropertyChangeListener {

    private JToolBar toolBar;
    private JButton btnAdd;
    private JButton btnRemove;
    private JButton btnPrint;
    private JButton btnExport;
    private JButton btnImport;

    public RepertoryUI() {
        super(Song.class, new ISTable(new SongTableModel()));
        initComponents();
        SodalisApplication.get().getStorageManager().registerComponent("repertoryUI", this);
        RepertorySettings.getInstance().addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(RepertorySettings.O_SELECTED_CATEGORIES)) {
            final List<Long> ids = (List<Long>) RepertorySettings.getInstance().getValue(RepertorySettings.O_SELECTED_CATEGORIES);

            categoryTreeComboBox.setSelectedCategories(CategoryDataManager.getInstance().getCategories(ids));
        }
    }

    @Override
    protected void currentObjectChanged() {
        refreshButtons();
    }

    private void initToolBar() {
        final AbstractButton categoryTreeButton = categoryTreeComponent.getShowCategoryTreeButton();
        final GridBagConstraints c = new GridBagConstraints();

        toolBar = UIUtils.createToolBar();
        toolBar.setLayout(new GridBagLayout());
        toolBar.setFloatable(false);

        btnAdd = new JButton(new AddSongAction());
        btnRemove = new JButton(new RemoveSongAction());
        btnPrint = new JButton(new PrintAction());
        btnExport = new JButton(new SongExportAction());
        btnImport = new JButton(new SongImportAction());

        initToolbarButton(btnAdd);
        initToolbarButton(btnRemove);
        initToolbarButton(btnPrint);
        initToolbarButton(btnImport);
        initToolbarButton(btnExport);
        initToolbarButton(categoryTreeButton);
        btnPrint.setEnabled(true);
        categoryTreeButton.setEnabled(true);

        categoryTreeComboBox = new CategoryTreeComboBox(RepertoryModule.class);
        categoryTreeComboBox.addChangeListener(new CategoryTreeComboBoxChangeListener(RepertorySettings.getInstance(), RepertoryContextManager.getInstance()));
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
        HideableSplitPane mainSplitPane = new HideableSplitPane(HideableSplitPane.VERTICAL_SPLIT, scrollPane, (controlPanel = new RepertoryControlPanel()).getControlComponent());

        table.setName("repertoryUI.table");
        initCategoryTreeComponent(RepertoryModule.class, scrollPane);
        initToolBar();
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setLayout(new BorderLayout());
        add(mainSplitPane, BorderLayout.CENTER);
        add(toolBar, BorderLayout.NORTH);
        if (RepertoryContextManager.getInstance().getFilterPanel() != null) {
            add(RepertoryContextManager.getInstance().getFilterPanel(), BorderLayout.EAST);
        }
        controlPanel.getControlComponent().setMinimumSize(new Dimension(200, 340));
        mainSplitPane.setName("repertoryUI.splitPane");
        mainSplitPane.setLeftText(LocaleManager.getString("songList"));
        mainSplitPane.setRightText(LocaleManager.getString("details"));
        mainSplitPane.setDividerLocation((int) 300);
        refreshButtons();
        TableSettings.getInstance().registerTable("SONG_TABLE", table);
    }

    private void setupInputMap() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK).toString());
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK).toString());
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK).toString());
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
            super("", IconFactory.getInstance().getIcon("print"));
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
            JRRewindableDataSource dataSource = new EntityPropertyJRDataSource<Person>(scala.collection.JavaConversions.asScalaBuffer(objects).toList());
            if (categoryShown) {
                dataSource = new CategoryWrapperDataSource(objects, (JRExtendedDataSource) dataSource);
            }

            TablePrintDialog dialog = new TablePrintDialog(
                    (TablePrintSettings) RepertorySettings.getInstance().getValue(RepertorySettings.O_DEFAULT_PRINT_SETTINGS),
                    EntityPropertyTranslatorManager.getTranslator(Song.class),
                    (List<TablePrintSettings>) RepertorySettings.getInstance().getValue(RepertorySettings.O_USER_PRINT_SETTINGS),
                    dataSource);

            if (categoryShown) {
                dialog.setGroups(categoryTreeComponent.getSelectedCategoryPath());
            }

            dialog.addTableSettingsListener(new TableSettingsListener() {

                @Override
                public void tableSettingsSaved(TablePrintSettings settings) {
                    final List<TablePrintSettings> userSettings =
                            (List<TablePrintSettings>) RepertorySettings.getInstance().getValue(RepertorySettings.O_USER_PRINT_SETTINGS);

                    for (TablePrintSettings tablePrintSettings : userSettings) {
                        if (tablePrintSettings.getName().equals(settings.getName())) {
                            userSettings.remove(tablePrintSettings);
                            break;
                        }
                    }
                    userSettings.add(settings);
                    RepertorySettings.getInstance().save();
                }

                @Override
                public void tableSettingsDeleted(TablePrintSettings settings) {
                    final List<TablePrintSettings> userSettings =
                            (List<TablePrintSettings>) RepertorySettings.getInstance().getValue(RepertorySettings.O_USER_PRINT_SETTINGS);

                    for (TablePrintSettings tablePrintSettings : userSettings) {
                        if (tablePrintSettings.getName().equals(settings.getName())) {
                            userSettings.remove(tablePrintSettings);
                            break;
                        }
                    }
                    RepertorySettings.getInstance().save();
                }
            });

            dialog.setVisible(true);
        }
    }
}
