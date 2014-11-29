package sk.magiksoft.sodalis.category.factory;

import sk.magiksoft.sodalis.category.entity.Category;

/**
 * @author wladimiiir
 */
public class CategoryFactory {
    private static CategoryFactory instance = null;

    private CategoryFactory() {
        instance = this;
    }

    public static synchronized CategoryFactory getInstance() {
        if (instance == null) {
            new CategoryFactory();
        }

        return instance;
    }

    public Category createCategory(Category parentCategory) {
        Category category = new Category(parentCategory);

        return category;
    }
}
