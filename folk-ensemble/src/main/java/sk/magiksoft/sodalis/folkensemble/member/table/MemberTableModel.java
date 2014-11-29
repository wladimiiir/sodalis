package sk.magiksoft.sodalis.folkensemble.member.table;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.sodalis.folkensemble.member.entity.EnsembleData;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.sodalis.person.entity.PrivatePersonData;
import sk.magiksoft.swing.ISTable;

import java.text.Collator;
import java.text.DateFormat;
import java.util.Comparator;

/**
 * @author wladimiiir
 */
public class MemberTableModel extends ObjectTableModel<Person> {
    private static final Comparator<Person> FIRSTNAME_COMPARATOR = new Comparator<Person>() {

        @Override
        public int compare(Person o1, Person o2) {
            return Collator.getInstance().compare(o1.getFirstName(), o2.getFirstName());
        }
    };
    private static final Comparator<Person> LASTNAME_COMPARATOR = new Comparator<Person>() {

        @Override
        public int compare(Person o1, Person o2) {
            return Collator.getInstance().compare(o1.getLastName(), o2.getLastName());
        }
    };
    private static final Comparator<Person> DATEOFBIRTH_COMPARATOR = new Comparator<Person>() {

        @Override
        public int compare(Person o1, Person o2) {
            return o1.getPersonData(PrivatePersonData.class).getBirthDate()
                    .compareTo(o2.getPersonData(PrivatePersonData.class).getBirthDate());
        }
    };
    private static final Comparator<Person> GROUP_COMPARATOR = new Comparator<Person>() {

        @Override
        public int compare(Person o1, Person o2) {
            return Collator.getInstance().compare(o1.getPersonData(EnsembleData.class).getEnsembleGroup().getGroupTypeToString(),
                    o2.getPersonData(EnsembleData.class).getEnsembleGroup().getGroupTypeToString());
        }
    };


    private static final String[] columnNames = new String[]{
            LocaleManager.getString("firstName"),
            LocaleManager.getString("lastName"),
            LocaleManager.getString("dateOfBirth"),
            LocaleManager.getString("EnsembleGroup.name"),
    };
    private static final Class[] columnClasses = new Class[]{
            ISTable.LEFT_ALIGNMENT_CLASS,
            ISTable.LEFT_ALIGNMENT_CLASS,
            ISTable.RIGHT_ALIGNMENT_CLASS,
            ISTable.LEFT_ALIGNMENT_CLASS
    };

    public MemberTableModel() {
        super(columnNames, columnClasses);
        columnIdentificators = new Object[]{
                "fullname",
                "birthdate",
                "address",
                "group"
        };
    }

    @Override
    public Object getValueAt(int row, int column) {
        Object obj = objects.get(row);
        if (!(obj instanceof Person)) {
            return "";
        }

        Person member = (Person) obj;

        switch (column) {
            case 0:
                return member.getFirstName();
            case 1:
                return member.getLastName();
            case 2:
                return DateFormat.getDateInstance().format(member.getPersonData(PrivatePersonData.class).getBirthDate().getTime());
            case 3:
                return member.getPersonData(EnsembleData.class).getEnsembleGroup().getGroupTypeToString();
        }

        return null;
    }

    @Override
    public Comparator getComparator(int column) {
        switch (column) {
            case 0:
                return FIRSTNAME_COMPARATOR;
            case 1:
                return LASTNAME_COMPARATOR;
            case 2:
                return DATEOFBIRTH_COMPARATOR;
            case 3:
                return GROUP_COMPARATOR;
            default:
                return super.getComparator(column);
        }
    }
}
