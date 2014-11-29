package sk.magiksoft.sodalis.folkensemble.member.report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import sk.magiksoft.sodalis.core.printing.ObjectDataSource;
import sk.magiksoft.sodalis.folkensemble.member.entity.EnsembleData;
import sk.magiksoft.sodalis.person.entity.Address;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.sodalis.person.entity.PrivatePersonData;

import java.util.List;

/**
 * @author wladimiiir
 */
public class MemberDataSource extends ObjectDataSource<Person> {

    public MemberDataSource(List<Person> members) {
        super(members);
    }

    @Override
    public Object getFieldValue(JRField field) throws JRException {
        final String fieldName = field.getName();

        if (entity == null) {
            return null;
        }
        if (fieldName.equals("fullname")) {
            return entity.getFullName(false);
        } else if (fieldName.equals("birthdate")) {
            return DATE_FORMAT.format(entity.getPersonData(PrivatePersonData.class).getBirthDate().getTime());
        } else if (fieldName.equals("address")) {
            List<Address> addresses = entity.getPersonData(PrivatePersonData.class).getAddresses();
            return addresses.isEmpty() ? null : addresses.get(0).toString();
        } else if (fieldName.equals("group")) {
            return entity.getPersonData(EnsembleData.class).getEnsembleGroup().getGroupTypeToString();
        }

        return "";
    }
}
