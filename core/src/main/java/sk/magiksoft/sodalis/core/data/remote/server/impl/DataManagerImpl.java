
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.data.remote.server.impl;

import com.thoughtworks.xstream.XStream;
import org.hibernate.*;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.type.SerializationException;
import org.hibernate.type.Type;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.data.DataListener;
import sk.magiksoft.sodalis.core.data.remote.server.DataManager;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.imex.SodalisTag;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.security.entity.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * @author wladimiiir
 */
public class DataManagerImpl extends UnicastRemoteObject implements DataManager {
    private static final int EVENT_ADD = 1;
    private static final int EVENT_UPDATE = 2;
    private static final int EVENT_REMOVE = 3;
    private static final String SESSION_ID = "session_id";
    private static SessionFactory sessionFactory;

    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = SodalisApplication.getDBManager().getConfiguration().buildSessionFactory();
        } catch (Exception ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed.\n" + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private Vector<DataListener> dataListeners = new Vector<DataListener>();
    private Map<String, Session> sessionMap = new HashMap<String, Session>();
    private Map<String, List<Event>> eventSessionMap = new HashMap<String, List<Event>>();

    public DataManagerImpl() throws RemoteException {
    }

    public void addDataListener(DataListener dataListener) {
        dataListeners.add(dataListener);
    }

    private Session openSession() {
        return sessionFactory.openSession();
    }

    private void closeSession(Session session) {
        session.close();
    }

    @Override
    public void closeSessionFactory() throws RemoteException {
        sessionFactory.close();
    }

    @Override public void resetSessionFactory() throws RemoteException {
        if(!sessionFactory.isClosed()){
            sessionFactory.close();
        }
        sessionFactory = SodalisApplication.getDBManager().getConfiguration().buildSessionFactory();
    }

    @Override
    public void closeSession(String sessionID) {
        Session session = sessionMap.remove(sessionID);
        List<Event> events = eventSessionMap.remove(sessionID);

        if (session != null) {
            session.getTransaction().commit();
            closeSession(session);
        }
        for (Event event : events) {
            switch (event.eventType) {
                case EVENT_ADD:
                    fireRecordsAdded(event.records);
                    break;
                case EVENT_UPDATE:
                    fireRecordsUpdated(event.records);
                    break;
                case EVENT_REMOVE:
                    fireRecordsRemoved(event.records);
                    break;
            }
        }
    }

    @Override
    public String registerNewSession() {
        Session session = openSession();
        String sessionID = SESSION_ID + System.currentTimeMillis();

        session.beginTransaction();
        sessionMap.put(sessionID, session);

        return sessionID;
    }

    @Override
    public <T> T initialize(T proxy) throws RemoteException {
        final Session session = openSession();
        try{
            if(proxy instanceof PersistentCollection){
                session.lock(((PersistentCollection) proxy).getOwner(), LockMode.NONE);
                Hibernate.initialize(proxy);
            }else{
                session.lock(proxy, LockMode.NONE);
            }
        }finally {
            closeSession(session);
        }
        return proxy;
    }

    @Override
    public String exportEntitiesToXML(SodalisTag sodalisTag, XStream xStream) throws RemoteException {
        final Session session = openSession();
        try {
            for (DatabaseEntity entity : sodalisTag.getCollection()) {
                session.lock(entity, LockMode.NONE);
            }
            return xStream.toXML(sodalisTag);
        } catch (Exception e){
            LoggerManager.getInstance().error(getClass(), e);
            return null;
        } finally {
            closeSession(session);
        }
    }

    @Override
    public void persistDatabaseEntities(List<? extends DatabaseEntity> entities) throws RemoteException {
        Session session = openSession();
        session.beginTransaction();

        for (DatabaseEntity entity : entities) {
            session.persist(entity);
        }
        session.getTransaction().commit();
        closeSession(session);
        fireRecordsAdded(entities);
    }

    @Override
    public DatabaseEntity addDatabaseEntity(User user, DatabaseEntity entity) {
        Session session = openSession();
        session.beginTransaction();

        if (user != null) {
            entity.setUpdater(user.getUserUID());
        }
        Long id = (Long) session.save(entity);
        entity.setId(id);
        session.getTransaction().commit();
        closeSession(session);
        fireRecordsAdded(Arrays.asList(entity));

        return entity;
    }

    @Override
    public List<? extends DatabaseEntity> addOrUpdateEntities(User user, List<? extends DatabaseEntity> entities) throws RemoteException {
        List<DatabaseEntity> updatedEntities = new ArrayList<DatabaseEntity>();
        List<DatabaseEntity> addedEntities = new ArrayList<DatabaseEntity>();
        Session session = openSession();
        session.beginTransaction();

        for (DatabaseEntity entity : entities) {
            if (user != null) {
                entity.setUpdater(user.getUserUID());
            }
            if (entity.getId() == null) {
                Long id = (Long) session.save(entity);
                entity.setId(id);
                addedEntities.add(entity);
            } else {
                session.update(entity);
                updatedEntities.add(entity);
            }
        }
        session.getTransaction().commit();
        closeSession(session);

        fireRecordsAdded(addedEntities);
        fireRecordsUpdated(updatedEntities);

        updatedEntities.addAll(addedEntities);

        return updatedEntities;
    }

    @Override
    public <T extends DatabaseEntity> T loadDatabaseEntity(T entity) throws RemoteException {
        Session session = openSession();

        session.beginTransaction();
        entity = (T) session.load(entity.getClass(), entity.getId());
        session.getTransaction().commit();
        closeSession(session);

        return (T) entity;
    }

    @Override
    public DatabaseEntity addDatabaseEntity(User user, String sessionID, DatabaseEntity entity) throws RemoteException {
        if (sessionID == null || !sessionMap.containsKey(sessionID)) {
            return addDatabaseEntity(user, entity);
        }

        Session session = sessionMap.get(sessionID);

        if (user != null) {
            entity.setUpdater(user.getUserUID());
        }
        Long id = (Long) session.save(entity);
        entity.setId(id);
        if (eventSessionMap.get(sessionID) == null) {
            eventSessionMap.put(sessionID, new ArrayList<Event>());
        }
        eventSessionMap.get(sessionID).add(new Event(EVENT_ADD, Collections.singletonList(entity)));

        return entity;
    }

    @Override
    public void addOrUpdateEntity(User user, DatabaseEntity entity) {
        Session session = openSession();
        boolean adding = entity.getId() == null;

        if (user != null) {
            entity.setUpdater(user.getUserUID());
        }
        session.beginTransaction();
        session.saveOrUpdate(entity);
        session.getTransaction().commit();
        closeSession(session);
        if (adding) {
            fireRecordsAdded(Arrays.asList(entity));
        } else {
            fireRecordsUpdated(Arrays.asList(entity));
        }
    }

    @Override
    public void addOrUpdateEntity(User user, String entityName, DatabaseEntity entity) {
        Session session = openSession();
        boolean adding = entity.getId() == null;

        if (user != null) {
            entity.setUpdater(user.getUserUID());
        }
        session.beginTransaction();
        session.saveOrUpdate(entityName, entity);
        session.getTransaction().commit();
        closeSession(session);
        if (adding) {
            fireRecordsAdded(Arrays.asList(entity));
        } else {
            fireRecordsUpdated(Arrays.asList(entity));
        }
    }

    @Override
    public boolean updateDatabaseEntity(User user, DatabaseEntity entity) {
        if (entity.getId() == null) {
            return false;
        }
        final Session session = openSession();

        if (user != null) {
            entity.setUpdater(user.getUserUID());
        }
        try{
            session.beginTransaction();
            session.update(entity);
            session.getTransaction().commit();
            fireRecordsUpdated(Arrays.asList(entity));
        }catch (Exception e){
            session.getTransaction().rollback();
            return false;
        }finally {
            closeSession(session);
        }
        return true;
    }

    @Override
    public boolean updateDatabaseEntity(User user, String sessionID, DatabaseEntity entity) {
        if (sessionID == null || !sessionMap.containsKey(sessionID)) {
            return updateDatabaseEntity(user, entity);
        }

        if (entity.getId() == null) {
            return false;
        }
        if (user != null) {
            entity.setUpdater(user.getUserUID());
        }
        Session session = sessionMap.get(sessionID);
        session.update(entity);
        if (eventSessionMap.get(sessionID) == null) {
            eventSessionMap.put(sessionID, new ArrayList<Event>());
        }
        eventSessionMap.get(sessionID).add(new Event(EVENT_UPDATE, Collections.singletonList(entity)));

        return true;
    }

    @Override
    public boolean removeDatabaseEntity(DatabaseEntity entity) {
        if (entity.getId() == null) {
            return false;
        }

        Session session = openSession();

        session.beginTransaction();
        try {
            session.delete(entity);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            closeSession(session);
        }

        fireRecordsRemoved(Arrays.asList(entity));

        return true;
    }

    @Override
    public boolean removeDatabaseEntity(String sessionID, DatabaseEntity entity) {
        if (sessionID == null || !sessionMap.containsKey(sessionID)) {
            return removeDatabaseEntity(entity);
        }

        if (entity.getId() == null) {
            return false;
        }
        Session session = sessionMap.get(sessionID);
        session.delete(entity);
        if (eventSessionMap.get(sessionID) == null) {
            eventSessionMap.put(sessionID, new ArrayList<Event>());
        }
        eventSessionMap.get(sessionID).add(new Event(EVENT_REMOVE, Collections.singletonList(entity)));

        return true;
    }

    protected void fireRecordsAdded(List<? extends DatabaseEntity> records) {
        for (DataListener dataListener : dataListeners) {
            dataListener.entitiesAdded(records);
        }
    }

    protected void fireRecordsUpdated(List<DatabaseEntity> records) {
        for (DataListener dataListener : dataListeners) {
            dataListener.entitiesUpdated(records);
        }
    }

    protected void fireRecordsRemoved(List<DatabaseEntity> records) {
        for (DataListener dataListener : dataListeners) {
            dataListener.entitiesRemoved(records);
        }
    }

    @Override
    public List getDatabaseEntities(Class clazz) {
        return getDatabaseEntities(clazz, null);
    }

    @Override
    public List getDatabaseEntities(Class clazz, String where) {
        List databaseEntities = new ArrayList();
        Session session = openSession();

        session.beginTransaction();
        databaseEntities = session.createQuery("from " + clazz.getName() + (where != null && !where.trim().isEmpty() ? " where " + where : "")).list();
        session.getTransaction().commit();
        closeSession(session);

        return databaseEntities;
    }

    @Override
    public List getDatabaseEntities(String query) {
        return getDatabaseEntities((List) null, query);
    }

    @Override
    public List getDatabaseEntities(List<DatabaseEntity> entities, String query) {
        List databaseEntities = new ArrayList();
        List queryResults;
        Session session = openSession();
        DatabaseEntity databaseEntity;

        session.beginTransaction();

        queryResults = session.createQuery(query).list();
        if (entities == null) {
            closeSession(session);
            return queryResults;
        }

        for (int i = 0; i < queryResults.size(); i++) {
            databaseEntity = (DatabaseEntity) queryResults.get(i);
            for (DatabaseEntity entity : entities) {
                if (databaseEntity.getId().equals(entity.getId())) {
                    databaseEntities.add(databaseEntity);
                    break;
                }
            }
        }

        session.getTransaction().commit();
        closeSession(session);

        return databaseEntities;
    }

    @Override
    public List getDatabaseEntities(String sessionID, String query) {
        return getDatabaseEntities(sessionID, null, query);
    }

    @Override
    public List getDatabaseEntities(String sessionID, List<DatabaseEntity> entities, String query) {
        if (sessionID == null || !sessionMap.containsKey(sessionID)) {
            return getDatabaseEntities(entities, query);
        }

        List databaseEntities = new ArrayList();
        List queryResults;
        Session session = sessionMap.get(sessionID);
        DatabaseEntity databaseEntity;

        queryResults = session.createQuery(query).list();
        if (entities == null) {
            return queryResults;
        }

        for (int i = 0; i < queryResults.size(); i++) {
            databaseEntity = (DatabaseEntity) queryResults.get(i);
            for (DatabaseEntity entity : entities) {
                if (databaseEntity.getId().equals(entity.getId())) {
                    databaseEntities.add(databaseEntity);
                    break;
                }
            }
        }

        return databaseEntities;
    }

    @Override
    public List getDatabaseEntities(String query, Object[] parameters, Type[] parameterTypes) {
        Session session = openSession();
        List list;
        Query q;

        session.beginTransaction();

        q = session.createQuery(query).setParameters(parameters, parameterTypes);
        try{
            list = q.list();
        }catch (SerializationException e){
            list = new ArrayList(0);
        }
        session.getTransaction().commit();
        closeSession(session);

        return list;
    }

    @Override
    public List getDatabaseEntities(String query, String[] names, Collection[] collections) {
        Session session = openSession();
        List list;
        Query q;

        session.beginTransaction();

        q = session.createQuery(query);
        for (int i = 0; i < names.length; i++) {
            q.setParameterList(names[i], collections[i]);
        }
        list = q.list();
        session.getTransaction().commit();
        closeSession(session);

        return list;
    }

    @Override
    public List getHQLQueryList(String query) {
        Session session = openSession();
        List list;

        session.beginTransaction();

        list = session.createQuery(query).list();

        session.getTransaction().commit();
        closeSession(session);

        return list;
    }

    @Override
    public List getSQLQueryList(String query) {
        Session session = openSession();
        List list;

        session.beginTransaction();

        list = session.createSQLQuery(query).list();

        session.getTransaction().commit();
        closeSession(session);

        return list;
    }

    @Override
    public int executeHQLQuery(String query) throws RemoteException {
        final Session session = openSession();
        int result;

        session.beginTransaction();
        result = session.createQuery(query).executeUpdate();
        session.getTransaction().commit();
        closeSession(session);

        return result;
    }

    @Override
    public int executeSQLQuery(String query) {
        final Session session = openSession();
        int result;

        session.beginTransaction();
        result = session.createSQLQuery(query).executeUpdate();
        session.getTransaction().commit();
        closeSession(session);

        return result;
    }

    @Override
    public boolean canDelete(DatabaseEntity entity) throws RemoteException {
        Session session = openSession();

        session.beginTransaction();
        try {
            session.delete(entity);
            session.flush();
        } catch (Exception e) {
            return false;
        } finally {
            session.getTransaction().rollback();
            closeSession(session);
        }

        return true;
    }

    private class Event {
        private int eventType;
        private List<DatabaseEntity> records;

        public Event(int eventType, List<DatabaseEntity> records) {
            this.eventType = eventType;
            this.records = records;
        }
    }
}