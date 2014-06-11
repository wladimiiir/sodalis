
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.person.entity;

import sk.magiksoft.sodalis.core.entity.*;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.search.FullText;

public class Contact extends AbstractDatabaseEntity{
    private static final long serialVersionUID = -1L;

    public enum ContactType{
        MOBILE_PHONE,
        HOME_PHONE,
        WORK_PHONE,
        EMAIL,
        HOME_INTERNET_PHONE,
        WORK_INTERNET_PHONE,
        SKYPE,
        ICQ,
        HOME_FAX,
        WORK_FAX,
        PAGER,
        ADDRESS,
        HOME_PAGE;

        @Override
        public String toString() {
            return LocaleManager.getString("Contact."+this.name());
        }
    }
    
    private ContactType contactType;
    @FullText private String contact;

    public Contact() {
        contactType=ContactType.MOBILE_PHONE;
        contact="";
    }

    public Contact(ContactType contactType, String contact) {
        this.contactType = contactType;
        this.contact = contact;
    }

    public String getContact() {
        return contact;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if(!(entity instanceof Contact)){
            return;
        }
        Contact c = (Contact) entity;

        this.contact = c.contact;
        this.contactType = c.contactType;
    }
}