
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.category.factory;

import sk.magiksoft.sodalis.category.entity.Category;

/**
 *
 * @author wladimiiir
 */
public class CategoryFactory {
    private static CategoryFactory instance = null;

    private CategoryFactory(){
        instance = this;
    }

    public static synchronized CategoryFactory getInstance(){
        if(instance == null){
            new CategoryFactory();
        }
        
        return instance;
    }

    public Category createCategory(Category parentCategory){
        Category category = new Category(parentCategory);

        return category;
    }
}