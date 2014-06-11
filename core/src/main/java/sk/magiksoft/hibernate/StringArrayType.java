
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.jar.Attributes.Name;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 *
 * @author wladimiiir
 */
public class StringArrayType implements UserType {

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.VARCHAR};
    }

    @Override
    public Class returnedClass() {
        return String[].class;
    }

    @Override
    public boolean equals(Object arg0, Object arg1) throws HibernateException {
        if (arg0 == null && arg1 == null) {
            return true;
        }
        if (arg0 != null && arg1 != null) {
            return Arrays.equals((String[]) arg0, (String[]) arg1);
        }

        return false;
    }

    @Override
    public int hashCode(Object arg0) throws HibernateException {
        return arg0.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet arg0, String[] arg1, Object arg2) throws HibernateException, SQLException {
        String value = arg0.getString(arg1[0]);

        if (value == null) {
            return new String[0];
        } else {
            return value.split(";");
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement arg0, Object arg1, int arg2) throws HibernateException, SQLException {
        String[] value = (String[]) arg1;
        StringBuilder stringValue = new StringBuilder();

        if (value == null) {
            arg0.setNull(arg2, Types.VARCHAR);
        } else {
            for (String string : value) {
                if (stringValue.length() > 0) {
                    stringValue.append(";");
                }
                stringValue.append(string);
            }
            arg0.setString(arg2, stringValue.toString());
        }
    }

    @Override
    public Object deepCopy(Object arg0) throws HibernateException {
        return Arrays.copyOf((String[]) arg0, ((String[]) arg0).length);
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object arg0) throws HibernateException {
        return (Serializable) arg0;
    }

    @Override
    public Object assemble(Serializable arg0, Object arg1) throws HibernateException {
        return arg0;
    }

    @Override
    public Object replace(Object arg0, Object arg1, Object arg2) throws HibernateException {
        return arg0;
    }
}