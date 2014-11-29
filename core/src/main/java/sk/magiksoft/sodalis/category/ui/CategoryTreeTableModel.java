package sk.magiksoft.sodalis.category.ui;

import sk.magiksoft.sodalis.category.CategoryDataManager;
import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.treetable.ObjectTreeTableModel;

/**
 * @author wladimiiir
 */
public class CategoryTreeTableModel extends ObjectTreeTableModel {
    private static final String[] COLUMN_NAMES = new String[]{
            LocaleManager.getString("name"),
            LocaleManager.getString("description")
    };

    public CategoryTreeTableModel() {
        super(new CategoryTreeNode(CategoryDataManager.getInstance().getRootCategory()), COLUMN_NAMES);
    }

    @Override
    public Object getValueAt(Object node, int column) {
        Category category = ((CategoryTreeNode) node).getDatabaseEntity();

        switch (column) {
            case 0:
                return category.getName();
            case 1:
                return category.getDescription();
            default:
                return "";
        }
    }


}
