
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
 * @author wladimiiir
 */
public class CategoryNodeWrapper {
    private Category category;
    private int depth;

    public CategoryNodeWrapper(Category category, int depth) {
        this.category = category;
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    public Category getCategory() {
        return category;
    }
}
