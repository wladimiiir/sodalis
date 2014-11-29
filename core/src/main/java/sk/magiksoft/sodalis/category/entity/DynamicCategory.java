package sk.magiksoft.sodalis.category.entity;

/**
 * @author wladimiiir
 */
public abstract class DynamicCategory extends Category {

    public DynamicCategory() {
    }

    public DynamicCategory(Category parentCategory, String name) {
        super(parentCategory, name);
    }

    public abstract boolean acceptCategorized(Categorized categorized);

    public abstract void refresh();
}
