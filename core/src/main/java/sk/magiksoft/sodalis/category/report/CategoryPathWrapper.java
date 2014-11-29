package sk.magiksoft.sodalis.category.report;

import sk.magiksoft.sodalis.category.entity.Categorized;

/**
 * @author wladimiiir
 */
public class CategoryPathWrapper {
    private String[] path;
    private Categorized categorized;

    public CategoryPathWrapper(String[] path, Categorized categorized) {
        this.path = path;
        this.categorized = categorized;
    }

    public Categorized getCategorized() {
        return categorized;
    }

    public String[] getPath() {
        return path;
    }
}
