
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.category.entity;

/**
 *
 * @author wladimiiir
 */
public abstract class DynamicCategory extends Category{

    public DynamicCategory() {
    }

    public DynamicCategory(Category parentCategory, String name) {
        super(parentCategory, name);
    }

    public abstract boolean acceptCategorized(Categorized categorized);

    public abstract void refresh();
}