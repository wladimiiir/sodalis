package sk.magiksoft.sodalis.category.entity;

import sk.magiksoft.sodalis.category.CategoryDataManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author wladimiiir
 */
public class CategoriesSettings implements Categorized, Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private boolean showUncategorized;
    private List<Long> categoryIDs;
    private transient List<Category> categories;

    public CategoriesSettings(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public List<Category> getCategories() {
        if (categories == null) {
            loadCategories(Collections.<Category>emptyList());
        }

        return categories;
    }

    @Override
    public void setCategories(List<Category> categories) {
        this.categories = categories;
        setupCategoryIDs();
    }

    public void setupCategoryIDs() {
        categoryIDs = new ArrayList<Long>();
        for (Category category : categories) {
            categoryIDs.add(category.id());
        }
    }

    public void loadCategories(List<Category> dynamicCategories) {
        categories = new ArrayList<Category>();
        if (categoryIDs == null) {
            return;
        }

        final Collection<Category> dbCategories = CategoryDataManager.getInstance().getCategories(categoryIDs);
        for (Long categoryID : categoryIDs) {
            if (categoryID < 0) {
                for (Category dynamicCategory : dynamicCategories) {
                    if (dynamicCategory.id().equals(categoryID)) {
                        categories.add(dynamicCategory);
                    }
                }
            } else {
                for (Category dbCategory : dbCategories) {
                    if (dbCategory.id().equals(categoryID)) {
                        categories.add(dbCategory);
                    }
                }
            }
        }

        categories.addAll(dbCategories);
    }

    public String getName() {
        return name;
    }

    public void setShowUncategorized(boolean showUncategorized) {
        this.showUncategorized = showUncategorized;
    }

    public boolean isShowUncategorized() {
        return showUncategorized;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CategoriesSettings other = (CategoriesSettings) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }


}
