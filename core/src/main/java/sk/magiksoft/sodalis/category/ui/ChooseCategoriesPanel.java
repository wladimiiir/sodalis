
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.category.ui;

import scala.collection.JavaConversions;
import sk.magiksoft.sodalis.category.entity.CategoriesSettings;
import sk.magiksoft.sodalis.category.entity.Categorized;
import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.category.settings.CategorySettings;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.module.Module;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;
import sk.magiksoft.sodalis.core.ui.NameDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author wladimiiir
 */
public class ChooseCategoriesPanel extends JPanel implements PropertyChangeListener {

    private static final String EMPTY_ITEM = "";
    private Class<? extends Module> moduleClass;
    private JComboBox settingsComboBox;
    private JCheckBox showUncategorized;
    private CategorizedEntityInfoPanel categorizedEntityInfoPanel;
    private boolean adjusting;

    public ChooseCategoriesPanel(Class<? extends Module> moduleClass) {
        this.moduleClass = moduleClass;
        initComponents();
        initListeners();
    }

    private void initComponents() {
        GridBagConstraints c = new GridBagConstraints();
        final JButton btnSave = new JButton(new SaveSettingsAction());
        final JButton btnDelete = new JButton(new DeleteSettingsAction());

        categorizedEntityInfoPanel = new CategorizedEntityInfoPanel(true) {
            private boolean initialized = false;

            @Override
            protected Class<? extends Module> getModuleClass() {
                return moduleClass;
            }

            @Override
            protected boolean acceptDrag() {
                return true;
            }

            @Override
            protected void addCategory(final Category category) {
                Enumeration<Category> en = (Enumeration<Category>) ((DefaultListModel) categorizedList.getModel()).elements();

                while (en.hasMoreElements()) {
                    Category element = en.nextElement();
                    if (element.id().equals(category.id())) {
                        return;
                    }
                }

                ((DefaultListModel) categorizedList.getModel()).addElement(category);
            }

            @Override
            public void reloadCategoryTree() {
                if(!initialized){
                    initialized = true;
                }else{
                    super.reloadCategoryTree();
                }
            }
        };
        categorizedEntityInfoPanel.setExpand(false);
        categorizedEntityInfoPanel.initLayout();

        setLayout(new GridBagLayout());
        settingsComboBox = new JComboBox();

        c.gridx = c.gridy = 0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 3);
        add(settingsComboBox, c);

        c.gridx++;
        c.insets = new Insets(5, 0, 5, 2);
        c.weightx = 0.0;
        add(btnSave, c);

        c.gridx++;
        c.insets = new Insets(5, 0, 5, 5);
        add(btnDelete, c);

        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 3;
        c.insets = new Insets(0, 5, 0, 5);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = c.weighty = 1.0;
        add(categorizedEntityInfoPanel, c);

        c.gridy++;
        c.weighty = 0.;
        c.insets = new Insets(0, 5, 5, 5);
        add(showUncategorized = new JCheckBox(LocaleManager.getString("showUncategorized")), c);

        reloadSettings();
        settingsComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() != ItemEvent.SELECTED || adjusting) {
                    return;
                }

                btnDelete.setEnabled(settingsComboBox.getSelectedItem() != EMPTY_ITEM);
                if (settingsComboBox.getSelectedItem() instanceof CategoriesSettings) {
                    loadSettings((CategoriesSettings) settingsComboBox.getSelectedItem());
                } else {
                    loadSettings(new CategoriesSettings(""));
                }
            }
        });
    }

    private void initListeners() {
        CategorySettings.getInstance().addPropertyChangeListener(this);
    }

    private void loadSettings(CategoriesSettings settings) {
        settings.loadCategories(JavaConversions.asJavaList(SodalisApplication.get().getModuleManager().getModuleByClass(moduleClass).getDynamicCategories()));
        showUncategorized.setSelected(settings.isShowUncategorized());
        categorizedEntityInfoPanel.setupPanel(settings);
        categorizedEntityInfoPanel.initData();
    }

    public void reloadCategoryTree() {
        categorizedEntityInfoPanel.reloadCategoryTree();
    }

    private void reloadSettings() {
        adjusting = true;
        final List<CategoriesSettings> settingses = (List<CategoriesSettings>) CategorySettings.getInstance().getValue(getSettingsKey());
        final Object selected = settingsComboBox.getSelectedItem();

        settingsComboBox.removeAllItems();
        settingsComboBox.addItem(EMPTY_ITEM);
        if (settingses != null) {
            for (CategoriesSettings categoriesSettings : settingses) {
                settingsComboBox.addItem(categoriesSettings);
            }
        }
        adjusting = false;
        settingsComboBox.setSelectedItem(selected);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!evt.getPropertyName().equals(getSettingsKey())) {
            return;
        }

        reloadSettings();
    }

    public boolean isShowUncategorized() {
        return showUncategorized.isSelected();
    }

    public List<Category> getSelectedCategories() {
        final List<Category> selectedCategories = new ArrayList<Category>();

        categorizedEntityInfoPanel.setupObject(new Categorized() {

            @Override
            public Long getId() {
                return null;
            }

            @Override
            public List<Category> getCategories() {
                return selectedCategories;
            }

            @Override
            public void setCategories(List<Category> categories) {
                selectedCategories.clear();
                selectedCategories.addAll(categories);
            }
        });

        return selectedCategories;
    }

    public void setSelectedCategories(final List<Category> categories) {
        categorizedEntityInfoPanel.setupPanel(new Categorized() {

            @Override
            public Long getId() {
                return null;
            }

            @Override
            public List<Category> getCategories() {
                return categories;
            }

            @Override
            public void setCategories(List<Category> categories) {
            }
        });
        categorizedEntityInfoPanel.initData();
    }

    private String getSettingsKey() {
        return moduleClass.getName() + "@" + CategorySettings.O_CATEGORIES_SETTINGSES;
    }

    private class SaveSettingsAction extends AbstractAction {

        public SaveSettingsAction() {
            super(LocaleManager.getString("save"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object selected = settingsComboBox.getSelectedItem();
            List<CategoriesSettings> settingses = (List<CategoriesSettings>) CategorySettings.getInstance().getValue(getSettingsKey());
            CategoriesSettings settings;

            if (selected instanceof CategoriesSettings) {
                settings = (CategoriesSettings) selected;
            } else {
                NameDialog dialog = new NameDialog(SodalisApplication.get().getMainFrame());
                String name = dialog.showDialog();

                if (name == null) {
                    return;
                }
                settings = new CategoriesSettings(name);
            }
            settings.setShowUncategorized(showUncategorized.isSelected());
            categorizedEntityInfoPanel.setupObject(settings);
            settings.setupCategoryIDs();

            if (settingses == null) {
                settingses = new ArrayList<CategoriesSettings>();
            }
            for (int i = settingses.size() - 1; i >= 0; i--) {
                CategoriesSettings categoriesSettings = settingses.get(i);
                if (categoriesSettings.equals(settings)) {
                    settingses.remove(categoriesSettings);
                }
            }
            settingses.add(settings);
            CategorySettings.getInstance().setValue(getSettingsKey(), settingses);
            CategorySettings.getInstance().save();
            settingsComboBox.setSelectedItem(settings);
        }
    }

    private class DeleteSettingsAction extends AbstractAction {

        public DeleteSettingsAction() {
            super(LocaleManager.getString("delete"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object selected = settingsComboBox.getSelectedItem();
            List<CategoriesSettings> settingses;

            if (!(selected instanceof CategoriesSettings)) {
                return;
            }
            int result = ISOptionPane.showConfirmDialog(ChooseCategoriesPanel.this,
                    LocaleManager.getString("deleteConfirm"), LocaleManager.getString("information"), ISOptionPane.YES_NO_OPTION);
            if (result != ISOptionPane.YES_OPTION) {
                return;
            }
            settingses = (List<CategoriesSettings>) CategorySettings.getInstance().getValue(getSettingsKey());
            settingses.remove(selected);
            settingsComboBox.removeItem(selected);
            CategorySettings.getInstance().setValue(getSettingsKey(), settingses);
            CategorySettings.getInstance().save();
        }
    }
}