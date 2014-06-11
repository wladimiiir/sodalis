
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.core.enumeration;

import sk.magiksoft.sodalis.category.entity.AbstractDynamicCategory;
import sk.magiksoft.sodalis.category.entity.Categorized;
import sk.magiksoft.sodalis.category.entity.DynamicCategory;
import sk.magiksoft.sodalis.core.locale.LocaleManager;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 11/26/10
 * Time: 3:30 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class EnumerationDynamicCategory  extends AbstractDynamicCategory {
    private Enumeration enumeration;

    public EnumerationDynamicCategory(Enumeration enumeration) {
        this.enumeration = enumeration;
        this.name = LocaleManager.getString(enumeration.getName());
    }

    public EnumerationDynamicCategory(String enumerationName) {
        this(EnumerationFactory.getInstance().getEnumeration(enumerationName));
    }

    @Override
    protected List<DynamicCategory> createChildCategories() {
        final List<DynamicCategory> categories = new LinkedList<DynamicCategory>();
        DynamicCategory category;

        for (final EnumerationEntry entry : enumeration.getEntries()) {
            category = new DynamicCategory(this, entry.getText()) {
                @Override
                public boolean acceptCategorized(Categorized categorized) {
                    return acceptEntryText(entry.getText(), categorized);
                }

                @Override
                public void refresh() {
                }
            };
            categories.add(category);
        }

        return categories;
    }

    protected abstract boolean acceptEntryText(String entryText, Categorized categorized);
}