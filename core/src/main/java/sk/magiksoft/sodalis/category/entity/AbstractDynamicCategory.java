package sk.magiksoft.sodalis.category.entity;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 * @since 2010/11/26
 */
public abstract class AbstractDynamicCategory extends DynamicCategory {
    private SoftReference<List<DynamicCategory>> childCategoriesReference;

    @Override
    public void refresh() {
        childCategoriesReference = null;
    }

    protected abstract List<DynamicCategory> createChildCategories();

    @Override
    public boolean acceptCategorized(Categorized categorized) {
        for (Category category : getChildCategories()) {
            if (((DynamicCategory) category).acceptCategorized(categorized)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<Category> getChildCategories() {
        if (childCategoriesReference == null || childCategoriesReference.get() == null) {
            childCategoriesReference = new SoftReference<List<DynamicCategory>>(createChildCategories());
        }

        return new ArrayList<Category>(childCategoriesReference.get());
    }
}
