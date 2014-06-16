
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

import java.util.List;

/**
 * @author wladimiiir
 */
public interface Categorized {
    Long getId();

    List<Category> getCategories();

    void setCategories(List<Category> categories);
}