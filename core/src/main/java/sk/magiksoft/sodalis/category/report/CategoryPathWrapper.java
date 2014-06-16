
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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