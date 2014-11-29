package sk.magiksoft.sodalis.person.entity;

import sk.magiksoft.hibernate.IntEnumUserType;
import sk.magiksoft.sodalis.person.entity.Person.Sex;

/**
 * @author wladimiiir
 */
public class SexEnumType extends IntEnumUserType<Sex> {

    public SexEnumType() {
        super(Sex.class, Sex.values());
    }
}
