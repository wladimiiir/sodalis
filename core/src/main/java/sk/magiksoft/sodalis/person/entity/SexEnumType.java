
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.person.entity;

import sk.magiksoft.hibernate.IntEnumUserType;
import sk.magiksoft.sodalis.person.entity.Person.Sex;

/**
 *
 * @author wladimiiir
 */
public class SexEnumType extends IntEnumUserType<Sex>{

    public SexEnumType() {
        super(Sex.class, Sex.values());
    }
}