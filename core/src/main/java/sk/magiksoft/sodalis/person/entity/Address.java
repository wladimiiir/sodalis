
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.person.entity;

import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.search.FullText;

/**
 * @author wladimiiir
 */
public class Address extends AbstractDatabaseEntity {
    private static final long serialVersionUID = -1L;

    @FullText
    private String description;
    @FullText
    private String street;
    @FullText
    private String number;
    @FullText
    private String town;
    @FullText
    private String postcode;
    @FullText
    private String state;

    public Address(String street, String number, String town, String postcode, String state) {
        this.street = street;
        this.number = number;
        this.town = town;
        this.postcode = postcode;
        this.state = state;
    }

    public Address() {
        street = "";
        number = "";
        town = "";
        postcode = "";
        state = "";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    @Override
    public String toString() {
        StringBuilder address = new StringBuilder();

        address.append(street).append(" ").append(number);
        if (!address.toString().trim().isEmpty()) {
            address.append(", ");
        }
        address.append(postcode).append(" ").append(town);

        return address.toString();
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof Address)) {
            return;
        }
        Address address = (Address) entity;
        this.number = address.number;
        this.postcode = address.postcode;
        this.state = address.state;
        this.street = address.street;
        this.town = address.town;
    }
}