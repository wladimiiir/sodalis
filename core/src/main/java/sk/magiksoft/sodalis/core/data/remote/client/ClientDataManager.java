
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.data.remote.client;

import com.thoughtworks.xstream.XStream;
import org.hibernate.type.Type;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.data.DataListener;
import sk.magiksoft.sodalis.core.data.remote.DataManagerProvider;
import sk.magiksoft.sodalis.core.data.remote.server.DataManager;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.imex.SodalisTag;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.security.LoginManagerService;
import sk.magiksoft.sodalis.core.security.entity.User;
import sk.magiksoft.utils.StringUtils;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author wladimiiir
 */
public class ClientDataManager implements DataManager {

    private transient DataManager dataManager;
    private transient User user = new DefaultUser();

    public ClientDataManager() {
        this.dataManager = DataManagerProvider.getDataManager();
    }

    public void addDataListener(DataListener listener) {
        DataManagerProvider.addDataListener(listener);
    }

    protected User getUser() {
        return user;
    }

    public <T extends DatabaseEntity> T addDatabaseEntity(T entity) {
        return (T) addDatabaseEntity(getUser(), entity);
    }

    @Override
    public DatabaseEntity addDatabaseEntity(User user, DatabaseEntity entity) {
        try {
            return dataManager.addDatabaseEntity(user, entity);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }

        return null;
    }

    @Override
    public <T extends DatabaseEntity> T loadDatabaseEntity(T entity) {
        try {
            return dataManager.loadDatabaseEntity(entity);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }

        return entity;
    }

    public List<? extends DatabaseEntity> addOrUpdateEntities(List<? extends DatabaseEntity> entities) {
        return addOrUpdateEntities(getUser(), entities);
    }

    @Override
    public <T> T initialize(T proxy) {
        try {
            return dataManager.initialize(proxy);
        } catch (RemoteException e) {
            LoggerManager.getInstance().error(getClass(), e);
        }
        return proxy;
    }

    @Override
    public void persistDatabaseEntities(List<? extends DatabaseEntity> entities) {
        try {
            dataManager.persistDatabaseEntities(entities);
        } catch (RemoteException e) {
            LoggerManager.getInstance().error(getClass(), e);
        }
    }

    @Override
    public String exportEntitiesToXML(SodalisTag sodalisTag, XStream xStream) {
        try {
            return dataManager.exportEntitiesToXML(sodalisTag, xStream);
        } catch (RemoteException e) {
            LoggerManager.getInstance().error(getClass(), e);
        }
        return null;
    }

    @Override
    public List<? extends DatabaseEntity> addOrUpdateEntities(User user, List<? extends DatabaseEntity> entities) {
        try {
            return dataManager.addOrUpdateEntities(user, entities);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }

        return entities;
    }

    public void addOrUpdateEntity(DatabaseEntity entity) {
        addOrUpdateEntity(getUser(), entity);
    }

    @Override
    public void addOrUpdateEntity(User user, DatabaseEntity entity) {
        try {
            dataManager.addOrUpdateEntity(user, entity);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }
    }

    public void addOrUpdateEntity(String entityName, DatabaseEntity entity) {
        addOrUpdateEntity(getUser(), entityName, entity);
    }

    @Override
    public void addOrUpdateEntity(User user, String entityName, DatabaseEntity entity) {
        try {
            dataManager.addOrUpdateEntity(user, entityName, entity);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }
    }

    public boolean updateDatabaseEntity(DatabaseEntity entity) {
        return updateDatabaseEntity(getUser(), entity);
    }

    @Override
    public boolean updateDatabaseEntity(User user, DatabaseEntity entity) {
        try {
            return dataManager.updateDatabaseEntity(user, entity);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }
        return false;
    }

    public boolean updateDatabaseEntity(String sessionID, DatabaseEntity entity) {
        return updateDatabaseEntity(getUser(), sessionID, entity);
    }

    @Override
    public boolean updateDatabaseEntity(User user, String sessionID, DatabaseEntity entity) {
        try {
            return dataManager.updateDatabaseEntity(user, sessionID, entity);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }
        return false;
    }

    @Override
    public boolean removeDatabaseEntity(DatabaseEntity entity) {
        try {
            return dataManager.removeDatabaseEntity(entity);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }
        return false;
    }

    @Override
    public boolean removeDatabaseEntity(String sessionID, DatabaseEntity entity) {
        try {
            return dataManager.removeDatabaseEntity(sessionID, entity);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }
        return false;
    }

    @Override
    public <T> List<T> getDatabaseEntities(Class<T> clazz) {
        try {
            return dataManager.getDatabaseEntities(clazz);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }
        return new ArrayList();
    }

    @Override
    public <T> List<T> getDatabaseEntities(Class<T> clazz, String where) {
        try {
            return dataManager.getDatabaseEntities(clazz, where);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }
        return new ArrayList();
    }

    @Override
    public List getDatabaseEntities(List<DatabaseEntity> entities, String query) {
        try {
            return dataManager.getDatabaseEntities(entities, query);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }
        return new ArrayList();
    }

    public <T extends DatabaseEntity> T getDatabaseEntity(Class<T> clazz, String where) {
        List<T> entities = getDatabaseEntities(clazz, where);

        return entities.isEmpty() ? null : entities.get(0);
    }

    public DatabaseEntity getDatabaseEntity(String query) {
        List entities = getDatabaseEntities(query);

        return entities.isEmpty() ? null : (DatabaseEntity) entities.get(0);
    }

    public <T extends DatabaseEntity> T getDatabaseEntity(Class<T> clazz, Long id) {
        List<T> entities = getDatabaseEntities(clazz, Collections.singletonList(id));

        return entities.isEmpty() ? null : entities.get(0);
    }

    @Override
    public List getDatabaseEntities(String sessionID, List<DatabaseEntity> entities, String query) {
        try {
            return dataManager.getDatabaseEntities(sessionID, entities, query);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }
        return new ArrayList();
    }

    @Override
    public List getDatabaseEntities(String query) {
        try {
            return dataManager.getDatabaseEntities(query);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }

        return new ArrayList();
    }

    @Override
    public List getDatabaseEntities(String sessionID, String query) {
        try {
            return dataManager.getDatabaseEntities(sessionID, query);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }

        return new ArrayList();
    }

    @Override
    public List getDatabaseEntities(String query, Object[] parameters, Type[] parameterTypes) {
        try {
            return dataManager.getDatabaseEntities(query, parameters, parameterTypes);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }

        return new ArrayList();
    }

    @Override
    public List getHQLQueryList(String query) {
        try {
            return dataManager.getHQLQueryList(query);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }
        return new ArrayList();
    }

    @Override
    public List getSQLQueryList(String query) {
        try {
            return dataManager.getSQLQueryList(query);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }
        return new ArrayList();
    }

    @Override
    public List getDatabaseEntities(String query, String[] names, Collection[] collections) {
        try {
            return dataManager.getDatabaseEntities(query, names, collections);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }
        return new ArrayList();
    }

    @Override
    public int executeHQLQuery(String query) throws RemoteException {
        try {
            return dataManager.executeHQLQuery(query);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }

        return -1;
    }

    @Override
    public int executeSQLQuery(String query) {
        try {
            return dataManager.executeSQLQuery(query);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }

        return -1;
    }

    @Override
    public void closeSessionFactory() {
        try {
            dataManager.closeSessionFactory();
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }
    }

    @Override
    public void resetSessionFactory() throws RemoteException {
        try {
            dataManager.resetSessionFactory();
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }
    }

    @Override
    public void closeSession(String sessionID) {
        try {
            dataManager.closeSession(sessionID);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }
    }

    @Override
    public String registerNewSession() {
        try {
            return dataManager.registerNewSession();
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }

        return null;
    }

    public DatabaseEntity addDatabaseEntity(String sessionID, DatabaseEntity entity) {
        return addDatabaseEntity(getUser(), sessionID, entity);
    }

    @Override
    public DatabaseEntity addDatabaseEntity(User user, String sessionID, DatabaseEntity entity) {
        try {
            return dataManager.addDatabaseEntity(user, sessionID, entity);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }
        return null;
    }

    @Override
    public boolean canDelete(DatabaseEntity entity) {
        try {
            return dataManager.canDelete(entity);
        } catch (RemoteException ex) {
            LoggerManager.getInstance().error(ClientDataManager.class, ex);
        }

        return false;
    }

    public <T extends DatabaseEntity> List<T> getDatabaseEntities(Class<T> clazz, List<Long> ids) {
        return ids.isEmpty()
                ? new ArrayList<T>()
                : getDatabaseEntities(clazz, "id in " + StringUtils.getStringList(ids, "(", ")", ","));
    }

    private class DefaultUser implements User, Serializable {

        @Override
        public String getUserUID() {
            try {
                return SodalisApplication.get().getService(LoginManagerService.class, LoginManagerService.SERVICE_NAME).getLoggedSubjectUID();
            } catch (Exception e) {
                return null;
            }
        }
    }
}