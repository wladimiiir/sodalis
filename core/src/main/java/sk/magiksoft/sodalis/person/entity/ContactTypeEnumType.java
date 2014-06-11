
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
import sk.magiksoft.sodalis.person.entity.Contact.ContactType;

/**
 *
 * @author wladimiiir
 */
public class ContactTypeEnumType extends IntEnumUserType<Contact.ContactType>{

    public ContactTypeEnumType() {
        super(ContactType.class, ContactType.values());
    }
}