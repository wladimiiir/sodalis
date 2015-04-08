package sk.magiksoft.sodalis.category.action;

import sk.magiksoft.sodalis.category.CategoryDataManager;
import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.category.ui.CategoryInfoPanel;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.action.ActionMessage;
import sk.magiksoft.sodalis.core.action.MessageAction;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.ui.OkCancelDialog;
import sk.magiksoft.sodalis.core.utils.UIUtils;
import sk.magiksoft.sodalis.icon.IconManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * @author wladimiiir
 */
public class AddCategoryAction extends MessageAction {

    private OkCancelDialog dialog;
    private CategoryInfoPanel categoryInfoPanel;
    private Category category;

    public AddCategoryAction() {
        super(null, IconManager.getInstance().getIcon("add"));
    }

    @Override
    public ActionMessage getActionMessage(List objects) {
        category = (Category) (objects == null || objects.isEmpty() || !(objects.get(0) instanceof Category)
                ? null : objects.get(0));

        return new ActionMessage(category != null,
                category == null ? LocaleManager.getString("noCategorySelected")
                        : LocaleManager.getString("addCategory"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (category == null) {
            return;
        }
        if (dialog == null) {
            initDialog();
        }

        final Category newCategory = new Category();
        newCategory.setParentCategory(category);
        categoryInfoPanel.setupPanel(newCategory);
        categoryInfoPanel.initData();
        dialog.setVisible(true);
    }

    private void initDialog() {
        dialog = new OkCancelDialog();
        categoryInfoPanel = new CategoryInfoPanel();

        categoryInfoPanel.initLayout();
        dialog.setOkAction(new OkAction());
        dialog.setModal(true);
        dialog.setTitle(LocaleManager.getString("newCategory"));
        dialog.setMainPanel(categoryInfoPanel);
        dialog.setSize(320, 200);
        UIUtils.makeISDialog(dialog);
        dialog.setLocationRelativeTo(SodalisApplication.get().getMainFrame());
    }

    private class OkAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            final Category newCategory = new Category();
            newCategory.setParentCategory(category);

            categoryInfoPanel.setupObject(newCategory);
            CategoryDataManager.getInstance().addDatabaseEntity(newCategory);
            dialog.setVisible(false);
        }
    }
}
