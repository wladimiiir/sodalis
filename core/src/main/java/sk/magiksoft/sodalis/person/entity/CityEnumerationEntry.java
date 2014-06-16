
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/


package sk.magiksoft.sodalis.person.entity;

import sk.magiksoft.sodalis.core.enumeration.EnumerationEntry;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 11/17/10
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class CityEnumerationEntry extends EnumerationEntry {
    private String zipCode;

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}