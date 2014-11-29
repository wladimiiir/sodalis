package sk.magiksoft.sodalis.core.license;

import sk.magiksoft.sodalis.person.entity.Person;

import java.io.File;
import java.io.Serializable;

/**
 * @author wladimiiir
 */
public interface License extends Serializable {
    public static final String PROPERTY_SUBSCRIBED_DAY_COUNT = "subscribedDayCount";

    Person getLicensePerson();

    Object getProperty(String key);

    Object getProperty(String key, Object defaultValue);

    boolean isRestricted(String key);

    boolean isDebugMode();

    void verifyFiles() throws LicenseException;

    boolean verifyFile(File file);

    String getLicenseNumber();

}
