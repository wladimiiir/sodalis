
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

import sk.magiksoft.sodalis.core.entity.*;

/**
 *
 * @author wladimiiir
 */
public class PersonWrapper extends AbstractDatabaseEntity{

    protected Person person;
    protected String personName="";

    public PersonWrapper() {
    }

    public PersonWrapper(Person person) {
        this.person = person;
    }

    public PersonWrapper(String personName) {
        this.personName = personName;
    }

    public PersonWrapper(PersonWrapper personWrapper) {
        this.person = personWrapper.person;
        this.personName = personWrapper.personName;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getPersonName() {
        return person==null ? personName : person.getFullName(false);
    }

    public void setPersonName(String PersonName) {
        this.personName = PersonName;
    }

    @Override
    public String toString() {
        return getPersonName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PersonWrapper other = (PersonWrapper) obj;
        if (this.person != other.person && (this.person == null || !this.person.equals(other.person))) {
            return false;
        }
        if ((this.personName == null)
                ? (other.personName != null)
                : !this.personName.equals(other.personName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.person != null
                ? this.person.hashCode()
                : 0);
        hash = 67 * hash + (this.personName != null
                ? this.personName.hashCode()
                : 0);
        return hash;
    }
    
    
    @Override
    public void updateFrom(DatabaseEntity entity) {
        if(!(entity instanceof PersonWrapper)){
            return;
        }
        
        PersonWrapper pw = (PersonWrapper) entity;
        this.person = pw.person;
        this.personName = pw.personName;
    }
}