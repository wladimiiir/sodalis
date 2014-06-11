
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.license;

import java.io.File;
import java.io.Serializable;
import sk.magiksoft.sodalis.person.entity.Person;

/**
 *
 * @author wladimiiir
 */
public interface License extends Serializable{
    public static final String PROPERTY_SUBSCRIBED_DAY_COUNT  = "subscribedDayCount";
    
    Person getLicensePerson();

    Object getProperty(String key);

    Object getProperty(String key, Object defaultValue);

    boolean isRestricted(String key);

    boolean isDebugMode();

    void verifyFiles() throws LicenseException;

    boolean verifyFile(File file);

    String getLicenseNumber();

}