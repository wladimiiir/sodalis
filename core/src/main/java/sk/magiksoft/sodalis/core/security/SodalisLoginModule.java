package sk.magiksoft.sodalis.core.security;

import sk.magiksoft.sodalis.core.factory.EntityFactory;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.security.entity.SodalisUser;
import sk.magiksoft.sodalis.core.security.entity.User;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.sodalis.person.entity.PrivatePersonData;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wladimiiir
 */
public class SodalisLoginModule implements LoginModule {

    private Subject subject;
    private CallbackHandler callbackHandler;
    private SodalisUser sodalisUser;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
    }

    @Override
    public boolean login() throws LoginException {
        NameCallback nameCallback = new NameCallback("Username");
        PasswordCallback passwordCallback = new PasswordCallback("Password", true);

        try {
            callbackHandler.handle(new Callback[]{nameCallback, passwordCallback});
        } catch (IOException ex) {
            LoggerManager.getInstance().error(SodalisLoginModule.class, ex);
            return false;
        } catch (UnsupportedCallbackException ex) {
            LoggerManager.getInstance().error(SodalisLoginModule.class, ex);
            return false;
        }

        String userName = nameCallback.getName();
        String password = String.valueOf(passwordCallback.getPassword());

        if (userName.trim().isEmpty()) {
            return false;
        }

        sodalisUser = getSodalisUser(userName);
        if (sodalisUser == null || !CryptoUtils.passwordsEqual(password, sodalisUser.getPassword(), sodalisUser.getUserName())) {
            throw new LoginException();
        }
        checkSodalisUserAttributes(sodalisUser);

        return true;
    }

    private void checkSodalisUserAttributes(SodalisUser sodalisUser) {
        boolean update = false;

        if (sodalisUser.getCredentialsMap() == null) {
            sodalisUser.setCredentialsMap(new HashMap<String, Serializable>());
            update = true;
        }
        if (!sodalisUser.getCredentialsMap().containsKey(User.CREDENTIAL_PERSON)) {
            sodalisUser.getCredentialsMap().put(User.CREDENTIAL_PERSON, EntityFactory.getInstance().createEntity(Person.class,
                    new Object[]{
                            PrivatePersonData.class,
                    }));
            update = true;
        }
        if (!update) {
            return;
        }
        updateSodalisUser(sodalisUser);
    }

    private void updateSodalisUser(SodalisUser user) {
        SecurityDataManager.getInstance().updateDatabaseEntity(user);
    }

    private SodalisUser getSodalisUser(String userName) {
        List list;
        SodalisUser user;

        list = SecurityDataManager.getInstance().getDatabaseEntities(SodalisUser.class, "userName='" + userName + "'");
        user = (SodalisUser) (list.isEmpty() ? null : list.get(0));

        return user;
    }

    @Override
    public boolean commit() throws LoginException {
        subject.getPrivateCredentials().clear();
        subject.getPublicCredentials().clear();

        subject.getPrivateCredentials().add(new SimpleEntry(User.CREDENTIAL_USERNAME, sodalisUser.getUserName()));
        subject.getPrivateCredentials().add(new SimpleEntry(User.CREDENTIAL_USER_UID, sodalisUser.getUserUID()));
        subject.getPublicCredentials().add(new SimpleEntry(User.CREDENTIAL_PERSON, sodalisUser.getCredentialsMap().get(User.CREDENTIAL_PERSON)));

        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        return true;
    }
}
