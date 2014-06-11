
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.data.remote.server;

import com.thoughtworks.xstream.XStream;
import org.hibernate.type.Type;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.imex.SodalisTag;
import sk.magiksoft.sodalis.core.security.entity.User;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

/**
 * @author wladimiiir
 */
public interface DataManager extends Remote, Serializable {

    public <T> T initialize(T proxy) throws RemoteException;

    public void persistDatabaseEntities(List<? extends DatabaseEntity> entities) throws RemoteException;

    public String exportEntitiesToXML(SodalisTag sodalisTag, XStream xStream) throws RemoteException;

    public DatabaseEntity addDatabaseEntity(User user, DatabaseEntity entity) throws RemoteException;

    public void addOrUpdateEntity(User user, DatabaseEntity entity) throws RemoteException;

    public List<? extends DatabaseEntity> addOrUpdateEntities(User user, List<? extends DatabaseEntity> entities) throws RemoteException;

    public boolean updateDatabaseEntity(User user, DatabaseEntity entity) throws RemoteException;

    boolean removeDatabaseEntity(DatabaseEntity entity) throws RemoteException;

    <T> List<T> getDatabaseEntities(Class<T> clazz) throws RemoteException;

    <T> List<T> getDatabaseEntities(Class<T> clazz, String where) throws RemoteException;

    <T extends DatabaseEntity> T loadDatabaseEntity(T entity) throws RemoteException;

    List getDatabaseEntities(List<DatabaseEntity> entities, String query) throws RemoteException;

    List getDatabaseEntities(String query) throws RemoteException;

    List getHQLQueryList(String query) throws RemoteException;

    List getSQLQueryList(String query) throws RemoteException;

    int executeHQLQuery(String query) throws RemoteException;

    int executeSQLQuery(String query) throws RemoteException;

    List getDatabaseEntities(String query, Object[] parameters, Type[] parameterTypes) throws RemoteException;

    List getDatabaseEntities(String query, String[] names, Collection[] collections) throws RemoteException;

    public List getDatabaseEntities(String sessionID, List<DatabaseEntity> entities, String query) throws RemoteException;

    public List getDatabaseEntities(String sessionID, String query) throws RemoteException;

    public boolean removeDatabaseEntity(String sessionID, DatabaseEntity entity) throws RemoteException;

    public boolean updateDatabaseEntity(User user, String sessionID, DatabaseEntity entity) throws RemoteException;

    public void addOrUpdateEntity(User user, String entityName, DatabaseEntity entity) throws RemoteException;

    void closeSession(String sessionID) throws RemoteException;

    String registerNewSession() throws RemoteException;

    public DatabaseEntity addDatabaseEntity(User user, String sessionID, DatabaseEntity entity) throws RemoteException;

    boolean canDelete(DatabaseEntity entity) throws RemoteException;

    void closeSessionFactory() throws RemoteException;

    void resetSessionFactory() throws RemoteException;
}