
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.printing;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import scala.collection.JavaConversions;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.data.DefaultDataManager;
import sk.magiksoft.sodalis.core.entity.Entity;
import sk.magiksoft.sodalis.core.entity.property.Translation;
import sk.magiksoft.sodalis.core.entity.property.Translator;
import sk.magiksoft.sodalis.core.factory.IconFactory;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.settings.Settings;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;
import sk.magiksoft.sodalis.core.ui.ImagePanel;
import sk.magiksoft.sodalis.core.ui.NameDialog;
import sk.magiksoft.sodalis.core.ui.OkCancelDialog;
import sk.magiksoft.sodalis.core.ui.property.EntityPropertyChooserDialog;
import sk.magiksoft.swing.ISTable;
import sk.magiksoft.swing.table.CheckBoxCellEditor;
import sk.magiksoft.swing.table.CheckBoxCellRenderer;
import sk.magiksoft.swing.table.ComboBoxTableCellEditor;
import sk.magiksoft.swing.text.TextPaneStyleControlPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author wladimiiir
 */
public class TablePrintDialog extends OkCancelDialog {
    private static boolean USE_FORM;
    private static boolean FORM_MODULE_PRESENT;

    /*static {
        try {
            Class.forName(Form.class.getName());
            USE_FORM = true;
            FORM_MODULE_PRESENT = SodalisApplication.get().getModuleManager().isModulePresent(FormModule.class);
            if (!FORM_MODULE_PRESENT) {
                LocaleManager.registerBundleBaseName("sk.magiksoft.sodalis.form.locale.form");
            }
        } catch (ClassNotFoundException e) {
            USE_FORM = false;
            FORM_MODULE_PRESENT = false;
        }
    }*/


    private Translator translator;
    private TablePrintSettings defaultPrintSettings;
    private List<TablePrintSettings> userPrintSettings;
    private JRRewindableDataSource dataSource;
    private String[] groups;
    //--user settings--
    private JComboBox cbxUserSettings;
    private JButton btnNewSettings;
    private JButton btnSaveSettings;
    private JButton btnDeleteSettings;
    //--page header--
    private JCheckBox chbShowPageHeader;
    private JTextPane tpnPageHeaderText;
    private TextPaneStyleControlPanel tpnStyleControlPanel;
    private JComboBox cbxHeaderForms;
    private JButton editFormButton;
    private ImagePanel imagePanel;
    private JCheckBox chbShowPageNumbers;
    private JCheckBox chbShowTotalCount;
    private JTextField tfdTotalCountLabel;
    //    private JCheckBox chbShowPageOne;
    private ISTable tblTableHeader;
    private PrintTableModel model;
    private Map<String, TableColumnWrapper> wrapperMap;
    private List<TableSettingsListener> listeners = new ArrayList<TableSettingsListener>();
    private EntityPropertyChooserDialog propertyChooserDialog;

    public TablePrintDialog(Settings settings, Translator<?> translator, JRRewindableDataSource dataSource) {
        this((TablePrintSettings) settings.getValue(Settings.O_DEFAULT_PRINT_SETTINGS), translator,
                (List<TablePrintSettings>) settings.getValue(Settings.O_USER_PRINT_SETTINGS), dataSource);
    }

    public TablePrintDialog(TablePrintSettings defaultPrintSettings, Translator<?> translator, List<TablePrintSettings> userPrintSettings, JRRewindableDataSource dataSource) {
        super(SodalisApplication.get().getMainFrame(), LocaleManager.getString("printSettings"));
        this.defaultPrintSettings = defaultPrintSettings;
        this.translator = translator;
        this.userPrintSettings = userPrintSettings;
        this.dataSource = dataSource;
        initComponents();
    }

    public void setGroups(String[] groups) {
        this.groups = groups;
    }

    private void initComponents() {
        final JPanel mainPanel = new JPanel(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 5, 0, 5);
        mainPanel.add(createUserSettingsPanel(), c);
        c.gridy++;
        c.weighty = 1.0;
        mainPanel.add(createPageHeaderPanel(), c);
        c.weighty = 0.0;
        c.gridy++;
        mainPanel.add(createTableHeaderPanel(), c);
        c.gridy++;
        mainPanel.add(createSettingsPanel(), c);

        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        c.gridy++;
        mainPanel.add(new JPanel(), c);

        setMainPanel(mainPanel);

        setOkAction(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                printReport();
            }
        });
        if (cbxUserSettings.getItemCount() > 0) {
            loadSettings((TablePrintSettings) cbxUserSettings.getItemAt(0));
        } else {
            createSettings();
        }

        setSize(650, 400);
        setMinimumSize(new Dimension(650, 400));
        setMaximumSize(new Dimension(655, 800));
        setLocationRelativeTo(null);
        setModalityType(ModalityType.DOCUMENT_MODAL);
    }

    public void addTableSettingsListener(TableSettingsListener listener) {
        listeners.add(listener);
    }

    public void removeTableSettingsListener(TableSettingsListener listener) {
        listeners.remove(listener);
    }

    private void fireTableSettingsSaved(TablePrintSettings settings) {
        for (TableSettingsListener tableSettingsListener : listeners) {
            tableSettingsListener.tableSettingsSaved(settings);
        }
    }

    private void fireTableSettingsDeleted(TablePrintSettings settings) {
        for (TableSettingsListener tableSettingsListener : listeners) {
            tableSettingsListener.tableSettingsDeleted(settings);
        }
    }

    private Component createUserSettingsPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        cbxUserSettings = new JComboBox();
        btnNewSettings = new JButton(LocaleManager.getString("new"), IconFactory.getInstance().getIcon("new"));
        btnSaveSettings = new JButton(LocaleManager.getString("save"), IconFactory.getInstance().getIcon("save"));
        btnDeleteSettings = new JButton(LocaleManager.getString("delete"), IconFactory.getInstance().getIcon("delete"));
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(cbxUserSettings, c);

        c.gridx++;
        c.weightx = 0.0;
        panel.add(btnNewSettings, c);

        c.gridx++;
        c.weightx = 0.0;
        panel.add(btnDeleteSettings, c);

        c.gridx++;
        c.weightx = 0.0;
        panel.add(btnSaveSettings, c);

        if (userPrintSettings != null) {
            for (TablePrintSettings tablePrintSettings : userPrintSettings) {
                cbxUserSettings.addItem(tablePrintSettings);
            }
        }

        cbxUserSettings.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() != ItemEvent.SELECTED) {
                    return;
                }
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        loadSettings((TablePrintSettings) cbxUserSettings.getSelectedItem());
                    }
                });
            }
        });
        btnNewSettings.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                createSettings();
            }
        });
        btnDeleteSettings.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSettings();
            }
        });
        btnSaveSettings.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveSettings();
            }
        });
        setModal(true);

        return panel;
    }

    private Component createPageHeaderPanel() {
        final JPanel pageHeaderPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        chbShowPageHeader = new JCheckBox(LocaleManager.getString("showPageHeader"));

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 0, 3, 0);
        pageHeaderPanel.add(chbShowPageHeader, c);

        /*if (USE_FORM) {
            imagePanel = new ImagePanel();
            imagePanel.setEditable(false);
            imagePanel.setNoImageInfoText("");
            imagePanel.setMinimumSize(new Dimension(200, 80));
            editFormButton = new JButton(LocaleManager.getString("edit"));
            editFormButton.addActionListener(new ActionListener() {
                private OkCancelDialog formEditorDialog;
                private FormEditor formEditor;

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (FORM_MODULE_PRESENT) {
                        new GoToEntityAction(SodalisApplication.get().getCurrentModuleClass(), FormModule.class) {
                            private List<Category> selectedCategories;
                            private Class<? extends InfoPanel> infoPanelClass;

                            @Override
                            protected boolean initialize(Context context) {
                                selectedCategories = context.getSelectedCategories();
                                infoPanelClass = context.getSelectedInfoPanelClass();
                                context.setSelectedCategories(Collections.singletonList(CategoryDataManager.getInstance().getInternalCategory(Form.HEADER_INTERNAL_ID())));
                                context.setSelectedEntities(Collections.singletonList(entity));
                                context.setSelectedInfoPanelClass(FormEditorInfoPanel.class);
                                setVisible(false);
                                return true;
                            }

                            @Override
                            protected void finalize(Context context) {
                                context.setSelectedCategories(selectedCategories);
                                context.setSelectedInfoPanelClass(infoPanelClass);
                                Object selectedItem = cbxHeaderForms.getSelectedItem();
                                cbxHeaderForms.removeAllItems();
                                for (Object headerForm : getHeaderForms(null)) {
                                    cbxHeaderForms.addItem(headerForm);
                                }
                                cbxHeaderForms.setSelectedItem(selectedItem);
                                setVisible(true);
                            }
                        }.goTo((Form) cbxHeaderForms.getSelectedItem());
                    } else {
                        final Form form = (Form) cbxHeaderForms.getSelectedItem();

                        if (formEditorDialog == null) {
                            formEditorDialog = new OkCancelDialog(TablePrintDialog.this, LocaleManager.getString("editor"));
                            formEditor = new FormEditor();
                            formEditorDialog.setMainPanel(formEditor.peer());
                            formEditorDialog.getOkButton().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    formEditor.setupForm(form);
                                    imagePanel.setImage(getSelectedHeaderFormImage());
                                    formEditorDialog.setVisible(false);
                                    TablePrintDialog.this.setVisible(true);
                                }
                            });
                            formEditorDialog.getCancelButton().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    formEditorDialog.setVisible(false);
                                    TablePrintDialog.this.setVisible(true);
                                }
                            });
                            formEditorDialog.setModal(true);
                            formEditorDialog.setSize(640, 480);
                            formEditorDialog.setLocationRelativeTo(null);
                        }

                        formEditor.setFormDrawings(form.getPages().toList());
                        TablePrintDialog.this.setVisible(false);
                        formEditorDialog.setVisible(true);
                    }
                }
            });
            cbxHeaderForms = new JComboBox(FORM_MODULE_PRESENT ? getHeaderForms(null) : new Vector(Collections.singletonList(new Form(Format.Custom(), Format.getWidthMM(Format.A4()), 40))));
            cbxHeaderForms.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        imagePanel.setImage(getSelectedHeaderFormImage());
                    }
                }
            });
            imagePanel.setImage(getSelectedHeaderFormImage());

            if (FORM_MODULE_PRESENT) {
                final JPanel formsPanel = new JPanel(new BorderLayout());
                formsPanel.add(cbxHeaderForms, BorderLayout.CENTER);
                formsPanel.add(editFormButton, BorderLayout.EAST);

                c.gridy++;
                c.weightx = 1.0;
                c.fill = GridBagConstraints.HORIZONTAL;
                pageHeaderPanel.add(formsPanel, c);
            } else {
                c.gridx++;
                c.anchor = GridBagConstraints.EAST;
                c.insets = new Insets(0, 3, 3, 0);
                pageHeaderPanel.add(editFormButton, c);
                c.gridx = 0;
                c.gridwidth = 2;
            }

            c.gridy++;
            c.weightx = 1.0;
            c.weighty = 1.0;
            c.insets = new Insets(0, 0, 0, 0);
            c.fill = GridBagConstraints.BOTH;
            pageHeaderPanel.add(imagePanel, c);
        } else */
        {
            JScrollPane scrollPane;
            tpnPageHeaderText = new JTextPane();
            tpnPageHeaderText.setEditorKit(new HTMLEditorKit());
            scrollPane = new JScrollPane(tpnPageHeaderText);
            scrollPane.setMinimumSize(new Dimension(200, 80));
            tpnStyleControlPanel = new TextPaneStyleControlPanel(tpnPageHeaderText);
            tpnStyleControlPanel.setEnabled(false);

            c.gridy++;
            c.weightx = 1.0;
            c.weighty = 1.0;
            c.insets = new Insets(0, 0, 0, 0);
            c.fill = GridBagConstraints.BOTH;
            pageHeaderPanel.add(scrollPane, c);

            c.gridy++;
            c.weighty = 0.0;
            pageHeaderPanel.add(tpnStyleControlPanel, c);
        }

        pageHeaderPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.getString("pageHeader")));

        chbShowPageHeader.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                /*if (USE_FORM) {
                    cbxHeaderForms.setEnabled(chbShowPageHeader.isSelected());
                    editFormButton.setEnabled(chbShowPageHeader.isSelected());
                } else */
                {
                    tpnPageHeaderText.setEnabled(chbShowPageHeader.isSelected());
                }
            }
        });

        return pageHeaderPanel;
    }

    private Component createSettingsPanel() {
        final JPanel settingPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        chbShowPageNumbers = new JCheckBox(LocaleManager.getString("showPageNumbers"));
        chbShowPageNumbers.setSelected(defaultPrintSettings.isShowPageNumbers());
        chbShowTotalCount = new JCheckBox();
        chbShowTotalCount.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                tfdTotalCountLabel.setEnabled(chbShowTotalCount.isSelected());
            }
        });
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.insets = new Insets(1, 3, 0, 0);
        c.anchor = GridBagConstraints.WEST;
        settingPanel.add(chbShowPageNumbers, c);
        final JPanel totalCountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
        totalCountPanel.add(chbShowTotalCount);
        totalCountPanel.add(tfdTotalCountLabel = new JTextField(LocaleManager.getString("totalCount"), 20));
        c.gridy++;
        c.insets = new Insets(1, 0, 1, 0);
        settingPanel.add(totalCountPanel, c);
        settingPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.getString("furtherSettings")));
        tfdTotalCountLabel.setEnabled(false);

        return settingPanel;
    }

    private Component createTableHeaderPanel() {
        final JPanel tableHeaderPanel = new JPanel(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        final JScrollPane scrollPane;
        final JButton btnAddColumn = new JButton("+");

        btnAddColumn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 1, 0, 0),
                btnAddColumn.getBorder()));
        btnAddColumn.setFocusPainted(false);
        tblTableHeader = new ISTable(model = new PrintTableModel()) {
            private TableCellRenderer centeredRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                    return c;
                }
            };
            private TableCellRenderer sumRenderer = new CheckBoxCellRenderer();
            private TableCellEditor sumEditor = new CheckBoxCellEditor();
            private TableCellEditor alignmentEditor = new ComboBoxTableCellEditor(TableColumnWrapper.Alignment.values()) {
                @Override
                public boolean isCellEditable(EventObject anEvent) {
                    return anEvent instanceof MouseEvent && ((MouseEvent) anEvent).getClickCount() == 1;
                }
            };

            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                switch (row) {
                    case 0:
                        return sumEditor;
                    case 1:
                        return alignmentEditor;
                    default:
                        return super.getCellEditor(row, column);
                }
            }

            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                switch (row) {
                    case 0:
                        return sumRenderer;
                    case 1:
                        return centeredRenderer;
                    default:
                        return super.getCellRenderer(row, column);
                }
            }
        };
        model.addObject(new ColumnProperties()); //sum
        model.addObject(new ColumnProperties()); //alignment
        tblTableHeader.setSelectionBackground(Color.WHITE);
        tblTableHeader.removeSortFunction();
        tblTableHeader.removeColumnVisibilityTableController();
        scrollPane = new JScrollPane(tblTableHeader);

        final JList rowHeader = new JList(new DefaultListModel());
        JLabel rowHeaderLabel = new JLabel("\u2211");
        rowHeaderLabel.setToolTipText(LocaleManager.getString("calculateSum"));
        ((DefaultListModel) rowHeader.getModel()).addElement(rowHeaderLabel);
        rowHeaderLabel = new JLabel(IconFactory.getInstance().getIcon("leftAlign"));
        rowHeaderLabel.setToolTipText(LocaleManager.getString("alignment"));
        ((DefaultListModel) rowHeader.getModel()).addElement(rowHeaderLabel);
        rowHeader.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                return (Component) value;
            }
        });
        scrollPane.setRowHeaderView(rowHeader);

        c.gridx = c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        tableHeaderPanel.add(scrollPane, c);
        c.gridx++;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTH;
        tableHeaderPanel.add(btnAddColumn, c);
        tableHeaderPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.getString("tableHeader")));
        btnAddColumn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addColumn();
            }
        });
        final JPopupMenu popupMenu = new JPopupMenu();
        final class DeleteAction extends AbstractAction {
            TableColumn column;

            DeleteAction() {
                super(LocaleManager.getString("delete"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                tblTableHeader.getColumnModel().removeColumn(column);
            }
        }
        final class EditColumnNameAction extends AbstractAction {
            TableColumn column;

            EditColumnNameAction() {
                super(LocaleManager.getString("changeName"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (column == null) {
                    return;
                }
                final String name = new NameDialog(TablePrintDialog.this).showDialog(column.getHeaderValue().toString());
                if (name == null) {
                    return;
                }
                column.setHeaderValue(name);
                tblTableHeader.getTableHeader().revalidate();
                tblTableHeader.getTableHeader().repaint();
            }
        }
        final DeleteAction deleteAction = new DeleteAction();
        final EditColumnNameAction editColumnNameAction = new EditColumnNameAction();
        popupMenu.add(deleteAction);
        popupMenu.add(editColumnNameAction);
        tblTableHeader.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 1 || !SwingUtilities.isRightMouseButton(e)) {
                    return;
                }
                final int index = tblTableHeader.columnAtPoint(e.getPoint());
                if (index >= 0) {
                    deleteAction.column = tblTableHeader.getColumnModel().getColumn(index);
                    editColumnNameAction.column = tblTableHeader.getColumnModel().getColumn(index);
                    popupMenu.show(tblTableHeader, e.getX(), e.getY());
                }
            }
        });
        tableHeaderPanel.setPreferredSize(new Dimension(100, 79));

        return tableHeaderPanel;
    }

    private void addColumn() {
        if (propertyChooserDialog == null) {
            propertyChooserDialog = new EntityPropertyChooserDialog(this, getTranslator());
        }

        propertyChooserDialog.setVisible(true);
        if (propertyChooserDialog.getResultAction() != OkCancelDialog.ACTION_OK) {
            return;
        }
        final List<Translation<?>> translations = scala.collection.JavaConversions.asJavaList(propertyChooserDialog.getSelectedTranslations());
        for (Translation translation : translations) {
            TableColumn column = new TableColumn(tblTableHeader.getColumnCount());
            column.setIdentifier(translation.key());
            column.setHeaderValue(translation.name());

            tblTableHeader.getColumnModel().addColumn(column);
        }
    }

    private Translator<?> getTranslator() {
        final CommonTranslator commonTranslator = new CommonTranslator();
        return new Translator() {
            @Override
            public scala.collection.immutable.List<Translation> getTranslations() {
                final scala.collection.immutable.List<Translation> entityTranslations = translator.getTranslations();
                final scala.collection.immutable.List<Translation<Entity>> commonTranslations = commonTranslator.getTranslations();
                final List<Translation> translations = new LinkedList<Translation>();

                translations.addAll(JavaConversions.asJavaList(entityTranslations));
                translations.addAll(JavaConversions.asJavaList(commonTranslations));

                return JavaConversions.asScalaBuffer(translations).toList();
            }
        };
    }

    private TablePrintDataSource createTablePrintDataSource() {
        final TablePrintDataSource printDataSource = new TablePrintDataSource();
        final Enumeration<TableColumn> columnEnum = tblTableHeader.getColumnModel().getColumns();
        final List<TableColumnWrapper> columns = new ArrayList<TableColumnWrapper>();
        TableColumn column;
        TableColumnWrapper columnWrapper;

        try {
            dataSource.moveFirst();
        } catch (JRException e) {
            LoggerManager.getInstance().error(getClass(), e);
        }
        int columnIndex = 0;
        while (columnEnum.hasMoreElements()) {
            column = columnEnum.nextElement();
            columnWrapper = new TableColumnWrapper(column.getIdentifier().toString(), column.getHeaderValue().toString(),
                    column.getWidth(),
                    wrapperMap.containsKey(column.getIdentifier().toString())
                            ? wrapperMap.get(column.getIdentifier().toString()).getValueClass()
                            : String.class,
                    (TableColumnWrapper.Alignment) model.getValueAt(1, tblTableHeader.convertColumnIndexToModel(columnIndex)),
                    (Boolean) model.getValueAt(0, tblTableHeader.convertColumnIndexToModel(columnIndex)));

            columns.add(columnWrapper);
            columnIndex++;
        }
        if (chbShowPageHeader.isSelected()) {
            /*if (USE_FORM) {
                printDataSource.setPageHeaderImage(getSelectedHeaderFormImage());
            } else*/
            {
                printDataSource.setPageHeaderComponent(imagePanel);
            }
        }
        printDataSource.setShowPageNumbers(chbShowPageNumbers.isSelected());
        printDataSource.setTotalCountLabel(chbShowTotalCount.isSelected() ? tfdTotalCountLabel.getText() : null);
        printDataSource.setDataSource(dataSource);
        printDataSource.setColumns(columns);

        return printDataSource;
    }

    /*private BufferedImage getSelectedHeaderFormImage() {
        final Form form = (Form) cbxHeaderForms.getSelectedItem();

        if (form == null) {
            return null;
        }

        final FormDrawing page = (FormDrawing) form.getPages().head();
        ImageOutputFormat format = new ImageOutputFormat();

        return format.toImage(page, page.getChildren(), new AffineTransform(), new Dimension(page.get(AttributeKeys.CANVAS_WIDTH).intValue(), page.get(AttributeKeys.CANVAS_HEIGHT).intValue()));
    }*/

    private void initWrapperMap(TablePrintSettings settings) {
        wrapperMap = new HashMap<String, TableColumnWrapper>();
        if (settings == null) {
            return;
        }
        for (TableColumnWrapper wrapper : settings.getTableColumnWrappers()) {
            wrapperMap.put(wrapper.getKey(), wrapper);
        }
    }


    private void setComponentsEnabled(boolean enabled) {
        getOkButton().setEnabled(enabled);
        /*if (USE_FORM) {
            cbxHeaderForms.setEnabled(enabled);
            editFormButton.setEnabled(enabled);
        } else */
        {
            imagePanel.setEnabled(enabled);
        }
        chbShowPageHeader.setEnabled(enabled);
        chbShowPageNumbers.setEnabled(enabled);
    }

    private void loadSettings(TablePrintSettings settings) {
        TableColumn column;
        int modelIndex = 0;

        while (tblTableHeader.getColumnCount() > 0) {
            tblTableHeader.removeColumn(tblTableHeader.getColumnModel().getColumn(0));
        }

        initWrapperMap(settings);
        if (settings == null) {
            setComponentsEnabled(false);
            return;
        }
        initPrintTableModel(settings);

        for (TableColumnWrapper tableColumnWrapper : settings.getTableColumnWrappers()) {
            column = new TableColumn(modelIndex++);
            column.setIdentifier(tableColumnWrapper.getKey());
            column.setHeaderValue(tableColumnWrapper.getHeaderValue());
            column.setWidth(tableColumnWrapper.getWidth());
            tblTableHeader.addColumn(column);
        }
        tblTableHeader.getColumnVisibilityTableController().initForTable(tblTableHeader);
//        imagePanel.setText(settings.getHeaderText());
        chbShowPageHeader.setSelected(settings.isShowPageHeader());
        chbShowPageNumbers.setSelected(settings.isShowPageNumbers());
        chbShowTotalCount.setSelected(settings.getTotalCountLabel() != null);
        tfdTotalCountLabel.setText(settings.getTotalCountLabel() != null ? settings.getTotalCountLabel() : LocaleManager.getString("totalCount"));

        /*if (USE_FORM) {
            if (FORM_MODULE_PRESENT) {
                if (settings.getFormID() == null) {
                    if (cbxHeaderForms.getItemCount() > 0) {
                        cbxHeaderForms.setSelectedIndex(0);
                    }
                } else {
                    for (int index = 0; index < cbxHeaderForms.getItemCount(); index++) {
                        Form form = (Form) cbxHeaderForms.getItemAt(index);
                        if (form.getId() != null && form.getId().equals(settings.getFormID())) {
                            cbxHeaderForms.setSelectedIndex(index);
                            break;
                        }
                    }
                }
            } else {
                cbxHeaderForms.removeAllItems();
                if (settings.getFormID() != null) {
                    final Form form = DefaultDataManager.getInstance().getDatabaseEntity(Form.class, settings.getFormID());
                    if (form != null) {
                        cbxHeaderForms.addItem(form);
                    } else {
                        cbxHeaderForms.addItem(new Form(Format.Custom(), Format.getWidthMM(Format.A4())-20, 40));
                    }
                } else {
                    cbxHeaderForms.addItem(new Form(Format.Custom(), Format.getWidthMM(Format.A4())-20, 40));
                }
            }

        }*/

        if (settings.getTableColumnSettings() != null) {
            SodalisApplication.get().getStorageManager().loadComponentState(settings.getTableColumnSettings(), tblTableHeader);
        }
        tblTableHeader.revalidate();
        tblTableHeader.repaint();
        setComponentsEnabled(true);
    }

    private void initPrintTableModel(TablePrintSettings settings) {
        final ColumnProperties sumProperties = model.getObject(0);
        final ColumnProperties alignmentProperties = model.getObject(1);

        for (int index = 0; index < settings.getTableColumnWrappers().size(); index++) {
            TableColumnWrapper wrapper = settings.getTableColumnWrappers().get(index);
            sumProperties.propertyMap.put(index, wrapper.isSum());
            alignmentProperties.propertyMap.put(index, wrapper.getAlignment());
        }
    }

    private void createSettings() {
        TablePrintSettings settings;

        settings = new TablePrintSettings(null);
        settings.setHeaderText(defaultPrintSettings.getHeaderText());
        settings.setShowPageHeader(defaultPrintSettings.isShowPageHeader());
        settings.setShowPageNumbers(defaultPrintSettings.isShowPageNumbers());
        settings.setTableColumnWrappers(new ArrayList<TableColumnWrapper>(defaultPrintSettings.getTableColumnWrappers()));
        cbxUserSettings.setSelectedItem(null);
        loadSettings(settings);
    }

    private void deleteSettings() {
        final TablePrintSettings settings = (TablePrintSettings) cbxUserSettings.getSelectedItem();
        int result;

        if (settings == null) {
            return;
        }
        result = ISOptionPane.showConfirmDialog(this,
                LocaleManager.getString("TablePrintDialog.deleteSettingsConfirm"), "", ISOptionPane.YES_NO_OPTION);
        if (result != ISOptionPane.YES_OPTION) {
            return;
        }

        cbxUserSettings.removeItem(settings);
        fireTableSettingsDeleted(settings);
    }

    private void saveSettings() {
        String name;
        TablePrintSettings settings;
        final TablePrintSettings selectedSettings = (TablePrintSettings) cbxUserSettings.getSelectedItem();

        if (selectedSettings == null) {
            name = new NameDialog(this).showDialog();
            if (name == null || name.trim().isEmpty()) {
                return;
            }
            for (int i = 0; i < cbxUserSettings.getItemCount(); i++) {
                settings = (TablePrintSettings) cbxUserSettings.getItemAt(i);
                if (settings.getName().equals(name)) {
                    ISOptionPane.showMessageDialog(null, LocaleManager.getString("TablePrintDialog.nameExists"));
                    return;
                }
            }
        } else {
            name = selectedSettings.getName();
        }

        settings = new TablePrintSettings(name);
        settings.setTableColumnWrappers(createCurrentTableColumnWrappers());

        /*if (USE_FORM) {
            Form form = (Form) cbxHeaderForms.getSelectedItem();
            if (form != null) {
                form.clearPages();
                if (form.getId() == null) {
                    form = (Form) DefaultDataManager.getInstance().addDatabaseEntity(form);
                } else {
                    DefaultDataManager.getInstance().updateDatabaseEntity(form);
                }
            }
            settings.setFormID(form == null ? null : form.getId());
        } else */
        {

        }

        settings.setShowPageHeader(chbShowPageHeader.isSelected());
        settings.setShowPageNumbers(chbShowPageNumbers.isSelected());
        settings.setTotalCountLabel(chbShowTotalCount.isSelected() ? tfdTotalCountLabel.getText() : null);
        tblTableHeader.getColumnVisibilityTableController().initForTable(tblTableHeader);
        settings.setTableColumnSettings(SodalisApplication.get().getStorageManager().saveComponentState(tblTableHeader));
        if (selectedSettings != null) {
            cbxUserSettings.removeItem(selectedSettings);
        }
        cbxUserSettings.addItem(settings);
        cbxUserSettings.setSelectedItem(settings);
        fireTableSettingsSaved(settings);
    }

    private List<TableColumnWrapper> createCurrentTableColumnWrappers() {
        final LinkedList<TableColumnWrapper> wrappers = new LinkedList<TableColumnWrapper>();
        final Enumeration<TableColumn> columns = tblTableHeader.getColumnModel().getColumns();
        TableColumn column;
        TableColumnWrapper wrapper;

        int index = 0;
        while (columns.hasMoreElements()) {
            column = columns.nextElement();
            wrapper = new TableColumnWrapper(column.getIdentifier().toString(), column.getHeaderValue().toString(), column.getWidth(),
                    wrapperMap.containsKey(column.getHeaderValue().toString()) ? wrapperMap.get(column.getHeaderValue().toString()).getValueClass() : String.class,
                    (TableColumnWrapper.Alignment) model.getValueAt(1, tblTableHeader.convertColumnIndexToModel(index)),
                    (Boolean) model.getValueAt(0, tblTableHeader.convertColumnIndexToModel(index)));
            wrappers.add(wrapper);
            index++;
        }

        return wrappers;
    }

    private Map<String, String> getGroupMap() {
        Map<String, String> groupMap = new HashMap<String, String>();
        int index;

        if (groups == null) {
            return groupMap;
        }
        index = 0;
        for (String group : groups) {
            groupMap.put(PrintingManager.GROUP_FIELD_NAME_PREFIX + index++, group);
        }

        return groupMap;
    }

    private void printReport() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                PrintingManager.getInstance().printTableReport(createTablePrintDataSource(), getGroupMap());
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (InterruptedException e) {
                    LoggerManager.getInstance().error(getClass(), e);
                } catch (ExecutionException e) {
                    ISOptionPane.showMessageDialog(LocaleManager.getString("printError"));
                    LoggerManager.getInstance().error(getClass(), e);
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        }.execute();
    }

    public Vector getHeaderForms(Long id) {
        return id == null ? new Vector(FormDataManager.getHeaderForms()) : new Vector(DefaultDataManager.getInstance().getDatabaseEntities(Form.class, Collections.singletonList(id)));
    }

    private class ColumnProperties {
        private Map<Integer, Object> propertyMap = new HashMap<Integer, Object>();
    }

    private static class PrintTableModel extends ObjectTableModel<ColumnProperties> {
        private PrintTableModel() {
            super(new Object[0]);
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public Object getValueAt(int row, int column) {
            final ColumnProperties properties = getObject(row);

            switch (row) {
                case 0:
                    return properties.propertyMap.containsKey(column) && (Boolean) properties.propertyMap.get(column);
                case 1:
                    return properties.propertyMap.containsKey(column)
                            ? properties.propertyMap.get(column)
                            : TableColumnWrapper.Alignment.LEFT;
                default:
                    return "";
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            final ColumnProperties properties = getObject(rowIndex);

            properties.propertyMap.put(columnIndex, aValue);
        }
    }
}