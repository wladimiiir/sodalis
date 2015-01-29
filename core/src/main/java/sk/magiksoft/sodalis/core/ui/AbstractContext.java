package sk.magiksoft.sodalis.core.ui;

import sk.magiksoft.sodalis.category.entity.Categorized;
import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.category.ui.CategoryTreeComboBox;
import sk.magiksoft.sodalis.core.context.Context;
import sk.magiksoft.sodalis.core.context.ContextManager;
import sk.magiksoft.sodalis.core.data.DataListener;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntityContainer;
import sk.magiksoft.sodalis.core.settings.Settings;
import sk.magiksoft.sodalis.core.controlpanel.ControlPanel;
import sk.magiksoft.sodalis.core.controlpanel.InfoPanel;
import sk.magiksoft.sodalis.core.utils.Utils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Collections;
import java.util.List;

/**
 * @author wladimiiir
 */
public abstract class AbstractContext extends JPanel implements Context, DataListener {

    protected Object currentObject = null;
    protected CategoryTreeComboBox categoryTreeComboBox;
    protected Class<? extends DatabaseEntity> contextClass;
    protected boolean adjusting = false;
    protected ControlPanel controlPanel;

    public AbstractContext(Class<? extends DatabaseEntity> contextClass) {
        super(true);
        this.contextClass = contextClass;
    }

    protected boolean acceptCategory(Categorized entity) {
        final List<Category> selected = categoryTreeComboBox.getSelectedCategories();
        for (Category category : selected) {
            if (!entity.getCategories().contains(category)) {
                return false;
            }
        }
        return true;
    }

    public Class<? extends DatabaseEntity> getContextClass() {
        return contextClass;
    }

    @Override
    public void setSelectedInfoPanelClass(Class<? extends InfoPanel> infoPanelClass) {
        if (controlPanel != null) {
            controlPanel.setSelectedInfoPanelClass(infoPanelClass);
        }
    }

    @Override
    public Class<? extends InfoPanel> getSelectedInfoPanelClass() {
        return controlPanel == null ? null : controlPanel.getSelectedInfoPanelClass();
    }

    public void setCategoryTreeComboBox(CategoryTreeComboBox categoryTreeComboBox) {
        this.categoryTreeComboBox = categoryTreeComboBox;
    }

    @Override
    public void setSelectedCategories(List<Category> categories) {
        if (categoryTreeComboBox == null) {
            return;
        }
        categoryTreeComboBox.setSelectedCategories(categories);
    }

    @Override
    public List<Category> getSelectedCategories() {
        return categoryTreeComboBox == null ? Collections.<Category>emptyList() : categoryTreeComboBox.getSelectedCategories();
    }

    protected void currentObjectChanged() {
    }

    protected boolean acceptEntity(DatabaseEntity entity) {
        return acceptEntity(entity, true);
    }

    protected boolean acceptEntity(DatabaseEntity entity, boolean checkContainerEntity) {
        return (contextClass.isAssignableFrom(entity.getClass()) || (checkContainerEntity && acceptInEntityContainer(entity)))
                && (categoryTreeComboBox == null || !(entity instanceof Categorized) || acceptCategory((Categorized) entity));
    }

    protected boolean acceptInEntityContainer(DatabaseEntity entity) {
        return !getEntities().isEmpty() && getEntities().get(0) instanceof DatabaseEntityContainer
                && ((DatabaseEntityContainer) getEntities().get(0)).acceptDatabaseEntity(entity.getClass());
    }

    protected Object loadObject(Object entity) {
        return entity;
    }

    protected AbstractButton initToolbarButton(AbstractButton button) {
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setEnabled(false);
        button.setPreferredSize(new Dimension(25, 25));
        return button;
    }

    protected class CategoryTreeComboBoxChangeListener implements ChangeListener {

        private Settings settings;
        private ContextManager contextManager;

        public CategoryTreeComboBoxChangeListener(Settings settings, ContextManager contextManager) {
            this.settings = settings;
            this.contextManager = contextManager;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            if (adjusting) {
                return;
            }

            final List<Long> selectedCategoryIDs = Utils.getIDList(categoryTreeComboBox.getSelectedCategories());
            final boolean selected = categoryTreeComboBox.isSelected();

            new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws Exception {
                    settings.setValue(Settings.O_SELECTED_CATEGORIES, selectedCategoryIDs, false);
                    settings.save();
                    contextManager.reloadData();
                    return null;
                }
            }.execute();

        }
    }
}
