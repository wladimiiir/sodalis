package sk.magiksoft.sodalis.folkensemble.member.data;

import sk.magiksoft.sodalis.category.entity.Categorized;
import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.category.entity.DynamicCategory;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.folkensemble.member.entity.EnsembleData;
import sk.magiksoft.sodalis.folkensemble.member.entity.EnsembleGroup;
import sk.magiksoft.sodalis.person.entity.Person;

import java.util.ArrayList;

/**
 * @author wladimiiir
 */
public class EnsembleGroupDynamicCategory extends DynamicCategory {

    public EnsembleGroupDynamicCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
        this.name = LocaleManager.getString("EnsembleGroup.name");
        setId(-200l);
        initChildCategories();
    }

    @Override
    public boolean acceptCategorized(Categorized categorized) {
        for (Category category : childCategories) {
            if (((DynamicCategory) category).acceptCategorized(categorized)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void refresh() {
    }

    protected boolean acceptEnsembleGroup(Categorized categorized, int groupType) {
        return categorized instanceof Person
                && ((Person) categorized).getPersonData(EnsembleData.class) != null
                && ((Person) categorized).getPersonData(EnsembleData.class).getEnsembleGroup().isGroupType(groupType);
    }

    private void initChildCategories() {
        Category category;

        childCategories = new ArrayList<Category>();
        category = new DynamicCategory(this, LocaleManager.getString("EnsembleGroup.dancer")) {

            @Override
            public boolean acceptCategorized(Categorized categorized) {
                return acceptEnsembleGroup(categorized, EnsembleGroup.GROUP_TYPE_DANCER);
            }

            @Override
            public void refresh() {
            }
        };
        category.setId(-201l);
        childCategories.add(category);

        category = new DynamicCategory(this, LocaleManager.getString("EnsembleGroup.singer")) {

            @Override
            public boolean acceptCategorized(Categorized categorized) {
                return acceptEnsembleGroup(categorized, EnsembleGroup.GROUP_TYPE_SINGER);
            }

            @Override
            public void refresh() {
            }
        };
        category.setId(-202l);
        childCategories.add(category);

        category = new DynamicCategory(this, LocaleManager.getString("EnsembleGroup.musician")) {

            @Override
            public boolean acceptCategorized(Categorized categorized) {
                return acceptEnsembleGroup(categorized, EnsembleGroup.GROUP_TYPE_MUSICIAN);
            }

            @Override
            public void refresh() {
            }
        };
        category.setId(-203l);
        childCategories.add(category);
    }

}
