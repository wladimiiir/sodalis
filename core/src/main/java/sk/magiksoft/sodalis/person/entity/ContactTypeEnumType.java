package sk.magiksoft.sodalis.person.entity;

import sk.magiksoft.hibernate.IntEnumUserType;
import sk.magiksoft.sodalis.person.entity.Contact.ContactType;

/**
 * @author wladimiiir
 */
public class ContactTypeEnumType extends IntEnumUserType<Contact.ContactType> {

    public ContactTypeEnumType() {
        super(ContactType.class, ContactType.values());
    }
}
