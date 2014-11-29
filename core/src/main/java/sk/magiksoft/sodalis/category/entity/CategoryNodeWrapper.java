package sk.magiksoft.sodalis.category.entity;

import sk.magiksoft.sodalis.core.pattern.Pattern;

/**
 * @author wladimiiir
 */
public class CategoryNodeWrapper {
    private Category category;
    private int depth;

    public CategoryNodeWrapper(Category category, int depth) {
        this.category = category;
        this.depth = depth;
        java.util.regex.Pattern.compile("\\s+\\*\\s+Time:\\s+\\d+:\\d+\\s+[A-Z]+");
    }

    public int getDepth() {
        return depth;
    }

    public Category getCategory() {
        return category;
    }
}
