
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/


package sk.magiksoft.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.collection.internal.PersistentBag;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.usertype.UserCollectionType;
import scala.collection.JavaConversions;
import scala.collection.mutable.Buffer;
import scala.collection.mutable.ListBuffer;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jun 20, 2010
 * Time: 9:19:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class ListBufferUserType implements UserCollectionType {
    @Override
    public PersistentCollection instantiate(SessionImplementor session, CollectionPersister persister) throws HibernateException {
        return new PersistentBag(session);
    }

    @Override
    public PersistentCollection wrap(SessionImplementor session, Object collection) {
        return new PersistentBag(session, JavaConversions.<Object>asJavaList((Buffer) collection));
    }

    @Override
    public Iterator getElementsIterator(Object collection) {
        return scala.collection.JavaConversions.asJavaList((Buffer) collection).iterator();
    }

    @Override
    public boolean contains(Object collection, Object entity) {
        return ((Buffer) collection).contains(entity);
    }

    @Override
    public Object indexOf(Object collection, Object entity) {
        return ((Buffer) collection).indexOf(entity);
    }

    @Override
    public Object replaceElements(Object original, Object target, CollectionPersister persister, Object owner, Map copyCache, SessionImplementor session) throws HibernateException {
        Buffer originalBuffer = (Buffer) original;
        Buffer targetBuffer = (Buffer) target;

        targetBuffer.clear();
        targetBuffer.appendAll(originalBuffer);

        return null;
    }

    @Override
    public Object instantiate(int anticipatedSize) {
        return new ListBuffer();
    }
}