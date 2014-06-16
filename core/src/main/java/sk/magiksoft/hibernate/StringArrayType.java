
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

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;

/**
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
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        String value = rs.getString(names[0]);

        if (value == null) {
            return new String[0];
        } else {
            return value.split(";");
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        String[] values = (String[]) value;
        StringBuilder stringValue = new StringBuilder();

        if (values == null) {
            st.setNull(index, Types.VARCHAR);
        } else {
            for (String string : values) {
                if (stringValue.length() > 0) {
                    stringValue.append(";");
                }
                stringValue.append(string);
            }
            st.setString(index, stringValue.toString());
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