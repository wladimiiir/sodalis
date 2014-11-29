package sk.magiksoft.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.EnhancedUserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author wladimiiir
 */
public class IntEnumUserType<E extends Enum<E>> implements EnhancedUserType {
    private Class<E> clazz = null;
    private E[] theEnumValues;

    /**
     * Contrary to the example mapping to a VARCHAR, this would
     *
     * @param c the class of the enum.
     * @param e The values of enum (by invoking .values()).
     */
    protected IntEnumUserType(Class<E> c, E[] e) {
        this.clazz = c;
        this.theEnumValues = e;
    }

    private static final int[] SQL_TYPES = {Types.SMALLINT};

    /**
     * simple mapping to a SMALLINT.
     */
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    @Override
    public Class returnedClass() {
        return clazz;
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Object assemble(Serializable cached, Object owner)
            throws HibernateException {
        return cached;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        final int val = rs.getShort(names[0]);
        E result = null;
        if (!rs.wasNull()) {
            try {
                for (int i = 0; i < theEnumValues.length && result == null; i++) {
                    if (theEnumValues[i].ordinal() == val) {
                        result = theEnumValues[i];
                    }
                }
            } catch (SecurityException e) {
                result = null;
            } catch (IllegalArgumentException e) {
                result = null;
            }
        }
        return result;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.SMALLINT);
        } else {
            st.setInt(index, ((Enum) value).ordinal());
        }
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y)
            return true;
        if (x == null || y == null)
            return false;
        return x.equals(y);
    }

    @Override
    public String objectToSQLString(Object value) {
        return String.valueOf(((Enum) value).ordinal());
    }

    @Override
    public String toXMLString(Object value) {
        return ((Enum) value).toString();
    }

    @Override
    public Object fromXMLString(String xmlValue) {
        for (Enum<E> enumValue : theEnumValues) {
            if (enumValue.toString().equals(xmlValue)) {
                return enumValue;
            }
        }
        return null;
    }
} 
