
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.person.data;

import sk.magiksoft.sodalis.category.entity.Categorized;
import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.category.entity.DynamicCategory;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.sodalis.person.entity.Person.Sex;

import java.util.ArrayList;

/**
 * @author wladimiiir
 */
public class SexDynamicCategory extends DynamicCategory {

    public SexDynamicCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
        this.name = LocaleManager.getString("sex");
        setId(-100l);

        initSexCategories();
    }

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
    public void refresh() {
    }

    private void initSexCategories() {
        Category dynamicCategory;
        int index = -1;

        childCategories = new ArrayList<Category>();
        for (final Sex sex : Sex.values()) {
            dynamicCategory = new DynamicCategory(this, sex.toString()) {

                @Override
                public boolean acceptCategorized(Categorized categorized) {
                    return categorized instanceof Person && ((Person) categorized).getSex().equals(sex);
                }

                @Override
                public void refresh() {
                }
            };
            dynamicCategory.setId(getId() - index--);
            childCategories.add(dynamicCategory);
        }
    }
}