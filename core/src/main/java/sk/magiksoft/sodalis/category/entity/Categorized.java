package sk.magiksoft.sodalis.category.entity;

import java.util.List;

/**
 * @author wladimiiir
 */
public interface Categorized {
    Long getId();

    List<Category> getCategories();

    void setCategories(List<Category> categories);
}
