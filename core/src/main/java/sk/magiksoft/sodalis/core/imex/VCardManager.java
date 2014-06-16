
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/


package sk.magiksoft.sodalis.core.imex;

import net.sf.vcard4j.java.AddressBook;
import net.sf.vcard4j.java.Type;
import net.sf.vcard4j.java.VCard;
import net.sf.vcard4j.java.type.ADR;
import net.sf.vcard4j.java.type.EMAIL;
import net.sf.vcard4j.java.type.N;
import net.sf.vcard4j.java.type.TITLE;
import net.sf.vcard4j.parser.DomParser;
import net.sf.vcard4j.parser.VCardParseException;
import org.w3c.dom.Document;
import sk.magiksoft.sodalis.core.factory.EntityFactory;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.person.entity.Address;
import sk.magiksoft.sodalis.person.entity.Contact;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.sodalis.person.entity.PrivatePersonData;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 1/9/11
 * Time: 7:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class VCardManager {
    private static VCardManager instance;

    public static VCardManager getInstance() {
        if (instance == null) {
            instance = new VCardManager();
        }
        return instance;
    }

    public List<Person> fromVCard(File vcfFile) {
        try {
            final DomParser parser = new DomParser();
            final FileReader reader = new FileReader(vcfFile);
            final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            final List<Person> persons = new LinkedList<Person>();
            VCard card;
            Type type;
            Person person;

            parser.parse(reader, document);

            final AddressBook book = new AddressBook(document);
            final Iterator<VCard> cards = book.getVCards();
            Iterator<Type> types;
            while (cards.hasNext()) {
                card = cards.next();
                person = EntityFactory.getInstance().createEntity(Person.class);
                types = card.getTypes("N");
                if (types.hasNext()) {
                    type = types.next();
                    person.setFirstName(((N) type).getGiven());
                    person.setLastName(((N) type).getFamily());
                }
                types = card.getTypes("TITLE");
                if (types.hasNext()) {
                    type = types.next();
                    person.setTitles(((TITLE) type).get());
                }
                types = card.getTypes("EMAIL");
                while (types.hasNext()) {
                    type = types.next();
                    person.getPersonData(PrivatePersonData.class).getContacts().add(new Contact(Contact.ContactType.EMAIL, ((EMAIL) type).get()));
                }
                types = card.getTypes("ADR");
                while (types.hasNext()) {
                    type = types.next();

                    String street;
                    String locality;
                    String postcode;
                    String country;
                    try {
                        street = ((ADR) type).getStreet();
                    } catch (Exception e) {
                        street = "";
                    }
                    try {
                        locality = ((ADR) type).getLocality();
                    } catch (Exception e) {
                        locality = "";
                    }
                    try {
                        postcode = ((ADR) type).getPcode();
                    } catch (Exception e) {
                        postcode = "";
                    }
                    try {
                        country = ((ADR) type).getCountry();
                    } catch (Exception e) {
                        country = "";
                    }

                    person.getPersonData(PrivatePersonData.class).getAddresses().add(new Address(
                            street,
                            "",
                            locality,
                            postcode,
                            country
                    ));
                }
                persons.add(person);
            }

            return persons;
        } catch (FileNotFoundException e) {
            LoggerManager.getInstance().error(getClass(), e);
        } catch (VCardParseException e) {
            LoggerManager.getInstance().error(getClass(), e);
        } catch (ParserConfigurationException e) {
            LoggerManager.getInstance().error(getClass(), e);
        } catch (IOException e) {
            LoggerManager.getInstance().error(getClass(), e);
        }
        return null;
    }
}