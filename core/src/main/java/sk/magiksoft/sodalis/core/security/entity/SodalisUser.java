package sk.magiksoft.sodalis.core.security.entity;

import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntityContainer;
import sk.magiksoft.sodalis.core.entity.PostCreation;
import sk.magiksoft.sodalis.core.factory.EntityFactory;
import sk.magiksoft.sodalis.core.security.CryptoUtils;
import sk.magiksoft.sodalis.person.entity.InternetData;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.sodalis.person.entity.PrivatePersonData;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wladimiiir
 */
public class SodalisUser extends AbstractDatabaseEntity implements User, DatabaseEntityContainer {
    private String userName = " ";
    private byte[] password;
    private String userUID;
    private boolean admin = false;
    private Map<String, Serializable> credentialsMap = new HashMap<String, Serializable>();

    @PostCreation
    public void initCredentialMap() {
        credentialsMap.put(CREDENTIAL_PERSON, EntityFactory.getInstance().createEntity(Person.class, new Object[]{PrivatePersonData.class, InternetData.class}));
    }

    @PostCreation
    public void initUserUID() {
        userUID = CryptoUtils.generateUIDString();
    }

    @Override
    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public Map<String, Serializable> getCredentialsMap() {
        return credentialsMap;
    }

    public void setCredentialsMap(Map<String, Serializable> credentialsMap) {
        this.credentialsMap = credentialsMap;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof SodalisUser)) {
            return;
        }
        SodalisUser user = (SodalisUser) entity;

        this.userName = user.userName;
        this.password = user.password;
    }

    @Override
    public String toString() {
        return "SodalisUser [" + userName + "]";
    }

    @Override
    public <T extends DatabaseEntity> boolean acceptDatabaseEntity(Class<T> clazz) {
        return clazz == Person.class;
    }

    @Override
    public <T extends DatabaseEntity> T getDatabaseEntity(Class<T> clazz) {
        if (clazz == Person.class) {
            return (T) credentialsMap.get(CREDENTIAL_PERSON);
        }

        return null;
    }

    @Override
    public <T extends DatabaseEntity> List<T> getDatabaseEntities(Class<T> clazz) {
        return clazz == Person.class ? Collections.singletonList(getDatabaseEntity(clazz)) : Collections.<T>emptyList();
    }


}
