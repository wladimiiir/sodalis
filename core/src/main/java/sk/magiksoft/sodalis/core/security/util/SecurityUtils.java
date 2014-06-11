
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.security.util;

import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.security.LoginManagerService;
import sk.magiksoft.sodalis.core.security.entity.SodalisUser;
import sk.magiksoft.sodalis.core.security.entity.User;
import sk.magiksoft.sodalis.person.entity.Person;

import javax.security.auth.Subject;
import java.util.Map.Entry;

/**
 * @author wladimiiir
 */
public class SecurityUtils {

    public static String getReadableUser(String userUID) {
        if (userUID == null) {
            return "";
        }

        SodalisUser user = SodalisApplication.get().getService(LoginManagerService.class, LoginManagerService.SERVICE_NAME).getUserForUID(userUID);
        if (user == null) {
            return "";
        }

        Person userPerson = (Person) user.getCredentialsMap().get(User.CREDENTIAL_PERSON);
        if (userPerson != null && !userPerson.getFullName(true).trim().isEmpty()) {
            return userPerson.getFullName(true);
        }

        return user.getUserName();
    }

    private SecurityUtils() {
    }

    public static Object getCredential(Subject subject, String key) {
        if (subject == null) {
            return null;
        }

        for (Entry entry : subject.getPrivateCredentials(Entry.class)) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        for (Entry entry : subject.getPublicCredentials(Entry.class)) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }

        return null;
    }
}