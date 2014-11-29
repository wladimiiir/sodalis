package sk.magiksoft.sodalis.folkensemble.member.ui;

import sk.magiksoft.sodalis.core.data.DataAdapter;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslatorManager;
import sk.magiksoft.sodalis.core.enumeration.*;
import sk.magiksoft.sodalis.core.filter.action.Filter;
import sk.magiksoft.sodalis.core.filter.action.FilterObjectListener;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel;
import sk.magiksoft.sodalis.folkensemble.member.entity.UniversityData;
import sk.magiksoft.sodalis.folkensemble.member.entity.property.UniversityDataPropertyTranslator;
import sk.magiksoft.sodalis.folkensemble.member.settings.MemberSettings;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.swing.ListTextField;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class UniversityDataInfoPanel extends AbstractInfoPanel implements PropertyChangeListener {

    static {
        EntityPropertyTranslatorManager.registerTranslator(Person.class, new UniversityDataPropertyTranslator());
    }

    private javax.swing.JLabel lblDepartment;
    private javax.swing.JLabel lblFaculty;
    private javax.swing.JLabel lblSpecialization;
    private javax.swing.JLabel lblType;
    private javax.swing.JLabel lblUniversity;
    private javax.swing.JLabel lblYear;
    private ListTextField<EnumerationEntry> tfdUniversity;
    private ListTextField<EnumerationEntry> tfdFaculty;
    private ListTextField<EnumerationEntry> tfdDepartment;
    private ListTextField<EnumerationEntry> tfdSpecialization;
    private JComboBox cbxStudiumType;
    private JSpinner spnYear;
    private Person uniPerson;

    public UniversityDataInfoPanel() {
    }

    @Override
    public void initData() {
        if (initialized) {
            return;
        }

        UniversityData data = uniPerson.getPersonData(UniversityData.class);
        tfdUniversity.setText(data.getUniversity());
        tfdFaculty.setText(data.getFaculty());
        tfdDepartment.setText(data.getDepartment());
        tfdSpecialization.setText(data.getSpecialization());
        spnYear.setValue(data.getYear() == 0 ? 1 : data.getYear());
        for (int i = 0; i < cbxStudiumType.getItemCount(); i++) {
            Object obj = cbxStudiumType.getItemAt(i);
            if (obj.toString().equals(data.getStudiumType())) {
                cbxStudiumType.setSelectedItem(obj);
                break;
            }
        }
        initialized = true;
    }


    protected Component createLayout() {
        final JPanel layoutPanel = new JPanel();

        lblUniversity = new javax.swing.JLabel();
        lblFaculty = new javax.swing.JLabel();
        lblYear = new javax.swing.JLabel();
        lblDepartment = new javax.swing.JLabel();
        lblSpecialization = new javax.swing.JLabel();
        lblType = new javax.swing.JLabel();
        tfdUniversity = new ListTextField<EnumerationEntry>(new EnumerationList(EnumerationFactory.getInstance().getEnumeration(Enumerations.UNIVERSITY_NAME)));
        final Filter<EnumerationEntry> facultyFilter = new Filter<EnumerationEntry>() {
            @Override
            public void addFilterObjectListener(final FilterObjectListener listener) {
                tfdUniversity.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        listener.filterObjectChanged(tfdUniversity.getCurrentObject());
                    }
                });
            }

            @Override
            public List<EnumerationEntry> filter(List<EnumerationEntry> objects) {
                final List<EnumerationEntry> filtered = new LinkedList<EnumerationEntry>();

                for (EnumerationEntry object : objects) {
                    if (object instanceof ReferenceEnumerationEntry && ((ReferenceEnumerationEntry) object).getReference()
                            .equals(String.valueOf(tfdUniversity.getCurrentObject().getFilterID()))) {
                        filtered.add(object);
                    }
                }

                return filtered;
            }
        };
        tfdFaculty = new ListTextField<EnumerationEntry>(new EnumerationList(EnumerationFactory.getInstance().getEnumeration(Enumerations.UNIVERSITY_FACULTY)), facultyFilter);
        tfdDepartment = new ListTextField<EnumerationEntry>(new EnumerationList(EnumerationFactory.getInstance().getEnumeration(Enumerations.UNIVERSITY_DEPARTMENT)));
        tfdSpecialization = new ListTextField<EnumerationEntry>(new EnumerationList(EnumerationFactory.getInstance().getEnumeration(Enumerations.UNIVERSITY_SPECIALIZATION)));
        spnYear = new JSpinner(new SpinnerNumberModel(1, 1, 6, 1));
        cbxStudiumType = new JComboBox(new EnumerationDataModel(EnumerationFactory.getInstance().getEnumeration(Enumerations.STUDIUM_TYPE)));
        cbxStudiumType.setEditable(false);
        cbxStudiumType.setSelectedIndex(0);
        layoutPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(LocaleManager.getString("universityData")));

        lblUniversity.setText(LocaleManager.getString("university"));

        lblFaculty.setText(LocaleManager.getString("faculty"));

        lblYear.setText(LocaleManager.getString("year"));

        lblDepartment.setText(LocaleManager.getString("department"));

        lblSpecialization.setText(LocaleManager.getString("specialization"));

        lblType.setText(LocaleManager.getString("studiumType"));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(layoutPanel);
        layoutPanel.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(lblSpecialization).addComponent(lblDepartment).addComponent(lblUniversity).addComponent(lblFaculty).addComponent(lblType)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(tfdUniversity, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE).addComponent(tfdFaculty, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE).addComponent(tfdDepartment, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE).addComponent(tfdSpecialization, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(cbxStudiumType, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE).addGap(18, 18, 18).addComponent(lblYear).addGap(18, 18, 18).addComponent(spnYear, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(tfdUniversity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblUniversity)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(tfdFaculty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblFaculty)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(tfdDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblDepartment)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(tfdSpecialization, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblSpecialization)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lblType).addComponent(cbxStudiumType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lblYear).addComponent(spnYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(77, Short.MAX_VALUE)));

        initListeners();

        return layoutPanel;
    }

    public void loadHistory() {
        tfdUniversity.setText(MemberSettings.getInstance().getString(MemberSettings.S_UNIVERSITY_HISTORY));
        tfdFaculty.setText(MemberSettings.getInstance().getString(MemberSettings.S_FACULTY_HISTORY));
        tfdDepartment.setText(MemberSettings.getInstance().getString(MemberSettings.S_DEPARTMENT_HISTORY));
        tfdSpecialization.setText(MemberSettings.getInstance().getString(MemberSettings.S_SPECIALIZATION_HISTORY));
    }

    public void saveHistory() {
        MemberSettings.getInstance().setValue(MemberSettings.S_UNIVERSITY_HISTORY, tfdUniversity.getText());
        MemberSettings.getInstance().setValue(MemberSettings.S_FACULTY_HISTORY, tfdFaculty.getText());
        MemberSettings.getInstance().setValue(MemberSettings.S_DEPARTMENT_HISTORY, tfdDepartment.getText());
        MemberSettings.getInstance().setValue(MemberSettings.S_SPECIALIZATION_HISTORY, tfdSpecialization.getText());
        MemberSettings.getInstance().save();
    }

    private void saveEnumerations() {
        if (!tfdUniversity.getText().isEmpty()) {
            EnumerationFactory.getInstance().getEnumeration(Enumerations.UNIVERSITY_NAME)
                    .addEntry(new EnumerationEntry(tfdUniversity.getText())).saveEnumeration();
        }
        if (!tfdFaculty.getText().isEmpty()) {
            EnumerationFactory.getInstance().getEnumeration(Enumerations.UNIVERSITY_FACULTY)
                    .addEntry(new EnumerationEntry(tfdFaculty.getText())).saveEnumeration();
        }
        if (!tfdDepartment.getText().isEmpty()) {
            EnumerationFactory.getInstance().getEnumeration(Enumerations.UNIVERSITY_DEPARTMENT)
                    .addEntry(new EnumerationEntry(tfdDepartment.getText())).saveEnumeration();
        }
        if (!tfdSpecialization.getText().isEmpty()) {
            EnumerationFactory.getInstance().getEnumeration(Enumerations.UNIVERSITY_SPECIALIZATION)
                    .addEntry(new EnumerationEntry(tfdSpecialization.getText())).saveEnumeration();
        }
    }

    private void initListeners() {
        DocumentListener documentListener = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!initialized) {
                    return;
                }
                fireEditing();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!initialized) {
                    return;
                }
                fireEditing();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (!initialized) {
                    return;
                }
                fireEditing();
            }
        };

        tfdUniversity.getDocument().addDocumentListener(documentListener);
        tfdFaculty.getDocument().addDocumentListener(documentListener);
        tfdDepartment.getDocument().addDocumentListener(documentListener);
        tfdSpecialization.getDocument().addDocumentListener(documentListener);
        cbxStudiumType.addItemListener(itemListener);
        spnYear.addChangeListener(changeListener);

        EnumerationFactory.getInstance().addDataListener(new DataAdapter() {

            @Override
            public void entitiesUpdated(List<? extends DatabaseEntity> entities) {
                for (int i = 0; i < entities.size(); i++) {
                    Object object = entities.get(i);
                    if (object instanceof Enumeration) {
                        Enumeration enumeration = (Enumeration) object;

                        if (enumeration.getName().equals(Enumerations.UNIVERSITY_NAME)) {
                            tfdUniversity.setItems(enumeration.getEntries());
                        } else if (enumeration.getName().equals(Enumerations.UNIVERSITY_FACULTY)) {
                            tfdFaculty.setItems(enumeration.getEntries());
                        } else if (enumeration.getName().equals(Enumerations.UNIVERSITY_DEPARTMENT)) {
                            tfdDepartment.setItems(enumeration.getEntries());
                        } else if (enumeration.getName().equals(Enumerations.UNIVERSITY_SPECIALIZATION)) {
                            tfdSpecialization.setItems(enumeration.getEntries());
                        }
                    }
                }
            }
        });

        MemberSettings.getInstance().addPropertyChangeListener(this);
    }

    @Override
    public String getPanelName() {
        return LocaleManager.getString("universityData");
    }

    public String getStudiumType() {
        return cbxStudiumType.getSelectedItem().toString();
    }

    public int getYear() {
        return new Integer(((SpinnerNumberModel) spnYear.getModel()).getValue().toString());
    }

    public String getDepartment() {
        return tfdDepartment.getText();
    }

    public String getFaculty() {
        return tfdFaculty.getText();
    }

    public String getSpecialization() {
        return tfdSpecialization.getText();
    }

    public String getUniversity() {
        return tfdUniversity.getText();
    }

    @Override
    public void setupObject(Object object) {
        if (!(object instanceof Person)) {
            return;
        }
        UniversityData universityMember = (UniversityData) ((Person) object).getPersonData(UniversityData.class);
        universityMember.setUniversity(getUniversity());
        universityMember.setFaculty(getFaculty());
        universityMember.setDepartment(getDepartment());
        universityMember.setSpecialization(getSpecialization());
        universityMember.setStudiumType(getStudiumType());
        universityMember.setYear(getYear());

        saveEnumerations();
    }

    @Override
    public void setupPanel(Object object) {
        if (!(object instanceof Person)) {
            return;
        }
        uniPerson = (Person) object;
        initialized = false;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(MemberSettings.S_DEPARTMENT_HISTORY)) {
            tfdDepartment.setText(evt.getNewValue().toString());
        } else if (evt.getPropertyName().equals(MemberSettings.S_FACULTY_HISTORY)) {
            tfdFaculty.setText(evt.getNewValue().toString());
        } else if (evt.getPropertyName().equals(MemberSettings.S_SPECIALIZATION_HISTORY)) {
            tfdSpecialization.setText(evt.getNewValue().toString());
        } else if (evt.getPropertyName().equals(MemberSettings.S_UNIVERSITY_HISTORY)) {
            tfdUniversity.setText(evt.getNewValue().toString());
        }
    }
}
