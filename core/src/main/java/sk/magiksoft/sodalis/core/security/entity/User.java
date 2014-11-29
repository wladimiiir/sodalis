package sk.magiksoft.sodalis.core.security.entity;

/**
 * @author wladimiiir
 */
public interface User {
    static final String CREDENTIAL_PERSON = "person";
    static final String CREDENTIAL_USERNAME = "username";
    static final String CREDENTIAL_USER_UID = "userUID";

    String getUserUID();
}
