package sk.magiksoft.sodalis.folkensemble.repertory.ui;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.sodalis.folkensemble.member.entity.EnsembleData;
import sk.magiksoft.sodalis.person.entity.PersonWrapper;
import sk.magiksoft.sodalis.person.entity.PrivatePersonData;
import sk.magiksoft.swing.ISTable;

/**
 * @author wladimiiir
 */
public class InterpreterTableModel extends ObjectTableModel<PersonWrapper> {

    private static final String[] columnNames = new String[]{
            LocaleManager.getString("firstName"),
            LocaleManager.getString("lastName"),
            LocaleManager.getString("dateOfBirth"),
            LocaleManager.getString("Group"),};
    private static final Class[] columnClasses = new Class[]{
            ISTable.LEFT_ALIGNMENT_CLASS,
            ISTable.LEFT_ALIGNMENT_CLASS,
            ISTable.RIGHT_ALIGNMENT_CLASS,
            ISTable.LEFT_ALIGNMENT_CLASS
    };

    public InterpreterTableModel() {
        super(columnNames, columnClasses);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PersonWrapper pw = getObject(rowIndex);

        switch (columnIndex) {
            case 0:
                return pw.getPerson() == null ? pw.getPersonName().substring(0, pw.getPersonName().lastIndexOf(" ")) : pw.getPerson().getFirstName();
            case 1:
                return pw.getPerson() == null ? pw.getPersonName().substring(pw.getPersonName().lastIndexOf(" ") + 1) : pw.getPerson().getLastName();
            case 2:
                return pw.getPerson() == null ? "" : DATE_FORMAT.format(pw.getPerson().getPersonData(PrivatePersonData.class).getBirthDate().getTime());
            case 3:
                return pw.getPerson() == null || pw.getPerson().getPersonData(EnsembleData.class) == null
                        ? ""
                        : pw.getPerson().getPersonData(EnsembleData.class).getEnsembleGroup().getGroupTypeToString();
            default:
                return "";
        }
    }
}
