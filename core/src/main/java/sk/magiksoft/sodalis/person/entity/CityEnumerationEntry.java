package sk.magiksoft.sodalis.person.entity;

import sk.magiksoft.sodalis.core.enumeration.EnumerationEntry;

/**
 * @author wladimiiir
 * @since 2010/11/17
 */
public class CityEnumerationEntry extends EnumerationEntry {
    private String zipCode;

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
