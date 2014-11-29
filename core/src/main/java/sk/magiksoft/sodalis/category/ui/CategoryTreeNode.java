package sk.magiksoft.sodalis.category.ui;

import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.core.treetable.DatabaseEntityTreeNode;

/**
 * @author wladimiiir
 */
public class CategoryTreeNode extends DatabaseEntityTreeNode<Category> {

    public CategoryTreeNode(Category category) {
        this(category, null);
    }

    public CategoryTreeNode(Category category, CategoryTreeNode parentNode) {
        super(parentNode, category);
        initChildren();
    }

    private void initChildren() {
        if (getDatabaseEntity() != null) {
            for (Category c : getDatabaseEntity().getChildCategories()) {
                children.add(createTreeNode(c));
            }
        }
    }

    @Override
    public void addDatabaseEntity(Category entity) {
        super.addDatabaseEntity(entity);

        boolean found = false;
        for (Category child : this.entity.getChildCategories()) {
            if (child.id().equals(entity.id())) {
                found = true;
                break;
            }
        }
        if (!found) {
            this.entity.getChildCategories().add(entity);
        }
    }

    @Override
    public DatabaseEntityTreeNode removeDatabaseEntity(Category entity) {
        for (Category category : this.entity.getChildCategories()) {
            if (category.id().equals(entity.id())) {
                this.entity.getChildCategories().remove(entity);
                break;
            }
        }
        return super.removeDatabaseEntity(entity);
    }

    @Override
    protected DatabaseEntityTreeNode<Category> createTreeNode(Category entity) {
        return new CategoryTreeNode(entity, this);
    }

    @Override
    public String toString() {
        return entity == null ? "" : entity.getName();
    }
}
