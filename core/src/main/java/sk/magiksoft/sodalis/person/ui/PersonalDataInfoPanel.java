package sk.magiksoft.sodalis.person.ui;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import org.hibernate.Hibernate;
import sk.magiksoft.sodalis.core.data.DefaultDataManager;
import sk.magiksoft.sodalis.core.enumeration.EnumerationDataModel;
import sk.magiksoft.sodalis.core.enumeration.EnumerationFactory;
import sk.magiksoft.sodalis.core.enumeration.Enumerations;
import sk.magiksoft.sodalis.icon.IconManager;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.ui.ContactComponent;
import sk.magiksoft.sodalis.core.ui.ImagePanel;
import sk.magiksoft.sodalis.core.controlpanel.AbstractInfoPanel;
import sk.magiksoft.sodalis.core.utils.Utils;
import sk.magiksoft.sodalis.person.entity.Address;
import sk.magiksoft.sodalis.person.entity.Contact;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.sodalis.person.entity.PrivatePersonData;
import sk.magiksoft.swing.ExtendedTabbedPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.List;

/**
 * @author wladimiiir
 */
public class PersonalDataInfoPanel extends AbstractInfoPanel {

    //--ui
    private ImagePanel ipnPhoto;
    private JPanel pnlAddress;
    private JTextField tfdFirstName;
    private JTextField tfdLastName;
    private JRadioButton rbtMale;
    private JRadioButton rbtFemale;
    private ContactComponent cmpContacts;
    private JDateChooser dchBirthDate;
    private JComboBox cbxTitles;
    private JTabbedPane tbpAddresses;
    private JTextField tfdTitle;
    private String infoTitle;
    private boolean contactsVisible;

    //--model
    private Person person;

    public PersonalDataInfoPanel() {
        this(true);
    }

    public PersonalDataInfoPanel(boolean contactsVisible) {
        this(contactsVisible, LocaleManager.getString("personalData"));
    }

    public PersonalDataInfoPanel(boolean contactsVisible, String infoTitle) {
        this.contactsVisible = contactsVisible;
        this.infoTitle = infoTitle;
    }

    @Override
    public void initData() {
        if (initialized) {
            return;
        }
        final PrivatePersonData privatePersonData = person.getPersonData(PrivatePersonData.class);
        tfdTitle.setText(person.getTitles());
        tfdFirstName.setText(person.getFirstName());
        tfdLastName.setText(person.getLastName());
        dchBirthDate.setCalendar(privatePersonData.getBirthDate());
        rbtFemale.setSelected(person.getSex() == Person.Sex.FEMALE);
        rbtMale.setSelected(person.getSex() == Person.Sex.MALE);
        cmpContacts.setItems(Utils.cloneItems(privatePersonData.getContacts()));
        if (!Hibernate.isInitialized(privatePersonData.getPhotoImageEntity())) {
            privatePersonData.setPhotoImageEntity(DefaultDataManager.getInstance().initialize(privatePersonData.getPhotoImageEntity()));
        }
        ipnPhoto.setImage((BufferedImage) privatePersonData.getPhoto());
        setAddresses(privatePersonData.getAddresses());
        initialized = true;
    }

    @Override
    protected Component createLayout() {
        final JPanel layoutPanel = new JPanel(new BorderLayout());
        final JPanel pnlMain = new JPanel();
        final JPanel pnlBirthDate = new JPanel();
        final JPanel pnlFiller = new JPanel();
        final JPanel pnlImage = new JPanel();
        final JPanel pnlContacts = new JPanel();
        final ButtonGroup btnGroup = new ButtonGroup();
        GridBagConstraints c;

        tfdTitle = new JTextField();
        tfdFirstName = new JTextField();
        tfdLastName = new JTextField();
        cbxTitles = System.getProperty("license") == null
                ? new JComboBox(new EnumerationDataModel(EnumerationFactory.getInstance().getEnumeration(Enumerations.PERSON_TITLE), true))
                : new JComboBox();
        dchBirthDate = new JDateChooser(new JTextFieldDateEditor("dd.MM.yyyy", "##.##.####", ' '));
        rbtMale = new JRadioButton();
        rbtFemale = new JRadioButton();
        cmpContacts = new ContactComponent();
        pnlAddress = new JPanel();
        tbpAddresses = new ExtendedTabbedPane(JTabbedPane.BOTTOM);
        ipnPhoto = new ImagePanel();

        ipnPhoto.setMinimumSize(new Dimension(100, 10));

        pnlMain.setLayout(new GridBagLayout());

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(15, 15, 2, 3);
        pnlMain.add(new JLabel(LocaleManager.getString("titles")), c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, 15, 2, 3);
        pnlMain.add(new JLabel(LocaleManager.getString("firstName")), c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, 15, 2, 3);
        pnlMain.add(new JLabel(LocaleManager.getString("lastName")), c);

//        tfdTitle.setPreferredSize(new Dimension(150, 21));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(15, 0, 2, 3);
        pnlMain.add(tfdTitle, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 0, 2, 2);
        pnlMain.add(tfdFirstName, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 2, 2);
        pnlMain.add(tfdLastName, c);

        cbxTitles.setPreferredSize(new Dimension(75, 21));
        c = new GridBagConstraints();
        c.insets = new Insets(15, 0, 2, 2);
        pnlMain.add(cbxTitles, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, 15, 2, 3);
        JLabel lblBirthDate = new JLabel(LocaleManager.getString("birthDateSC"));
        lblBirthDate.setToolTipText(LocaleManager.getString("birthDate"));
        pnlMain.add(lblBirthDate, c);

        pnlBirthDate.setLayout(new GridBagLayout());
        dchBirthDate.setPreferredSize(new Dimension(130, 21));
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;
        pnlBirthDate.add(dchBirthDate, c);
//        pnlBirthDate.add(dspBirthDate, c);

        btnGroup.add(rbtMale);
        rbtMale.setText(LocaleManager.getString("male"));
        rbtMale.setIcon(IconManager.getInstance().getIcon("male"));
        rbtMale.setSelected(true);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        pnlBirthDate.add(rbtMale, c);

        btnGroup.add(rbtFemale);
        rbtFemale.setText(LocaleManager.getString("female"));
        rbtFemale.setIcon(IconManager.getInstance().getIcon("female"));
        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        pnlBirthDate.add(rbtFemale, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 0, 2, 2);
        pnlMain.add(pnlBirthDate, c);

        fillAddressPanel();

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 3;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 15, 5, 2);
        pnlMain.add(pnlAddress, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 4;
        c.weighty = 1.0;
        pnlMain.add(pnlFiller, c);

        pnlImage.setLayout(new BorderLayout());
        pnlImage.setPreferredSize(new Dimension(150, 100));
        pnlImage.setBorder(BorderFactory.createTitledBorder(LocaleManager.getString("photo")));
        pnlImage.add(ipnPhoto, BorderLayout.CENTER);

        c = new GridBagConstraints();
        c.gridx = 3;
        c.gridy = 0;
        c.gridheight = 4;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.VERTICAL;
        c.insets = new Insets(5, 0, 0, 15);
        pnlMain.add(pnlImage, c);

        c = new GridBagConstraints();
        c.gridy = 4;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 10, 5, 15);
        pnlMain.add(pnlContacts, c);

        pnlContacts.setBorder(BorderFactory.createTitledBorder(LocaleManager.getString("contacts")));
        pnlContacts.setLayout(new BorderLayout());
        pnlContacts.add(cmpContacts, BorderLayout.CENTER);
        cmpContacts.setPreferredSize(new Dimension(270, 50));

        pnlMain.setBorder(BorderFactory.createTitledBorder(infoTitle));
        layoutPanel.add(pnlMain, BorderLayout.CENTER);
        if (!contactsVisible) {
            pnlContacts.setVisible(false);
        }

        initListeners();

        return layoutPanel;
    }

    private void fillAddressPanel() {
        JPanel pnlButtons = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JButton btnAddAddress = new JButton("") {

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.drawLine(3, 6, 9, 6);
                g.drawLine(6, 3, 6, 9);
            }
        };
        JButton btnRemoveAddress = new JButton("") {

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.drawLine(3, 6, 9, 6);
            }
        };

        btnAddAddress.setPreferredSize(new Dimension(14, 14));
        btnAddAddress.setFocusPainted(false);
        btnRemoveAddress.setPreferredSize(new Dimension(14, 14));
        btnRemoveAddress.setFocusPainted(false);

        c.gridx = c.gridy = 0;
        c.weightx = 1.0;
        pnlButtons.add(new JPanel(), c);
        c.gridx++;
        c.weightx = 0.0;
        c.insets = new Insets(1, 1, 1, 1);
        pnlButtons.add(btnAddAddress, c);
        c.gridx++;
        pnlButtons.add(btnRemoveAddress, c);
        pnlAddress.setBorder(BorderFactory.createTitledBorder(LocaleManager.getString("address")));
        pnlAddress.setLayout(new BorderLayout());
        pnlAddress.setPreferredSize(new Dimension(200, 150));

        pnlAddress.add(tbpAddresses, BorderLayout.CENTER);
        pnlAddress.add(pnlButtons, BorderLayout.SOUTH);

        tbpAddresses.addPropertyChangeListener(ExtendedTabbedPane.PROPERTY_TAB_TITLE, propertyChangeListener);
        btnAddAddress.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Address address = createAddress();
                AddressPanel addressPanel = new AddressPanel(address);

                addressPanel.addDocumentListener(documentListener);
                tbpAddresses.addTab(address.getDescription(), addressPanel);
                fireEditing();
            }
        });
        btnRemoveAddress.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (tbpAddresses.getSelectedIndex() < 0 || tbpAddresses.getTabCount() <= 1) {
                    return;
                }

                tbpAddresses.remove(tbpAddresses.getSelectedIndex());
                fireEditing();
            }
        });
    }

    private void initListeners() {
        tfdTitle.getDocument().addDocumentListener(documentListener);
        tfdFirstName.getDocument().addDocumentListener(documentListener);
        tfdLastName.getDocument().addDocumentListener(documentListener);
        dchBirthDate.addPropertyChangeListener("date", propertyChangeListener);
        rbtFemale.addActionListener(actionListener);
        rbtMale.addActionListener(actionListener);
        cmpContacts.addItemComponentListener(itemComponentListener);
        ipnPhoto.addImagePanelListener(imagePanelListener);
        cbxTitles.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (cbxTitles.getSelectedItem() == null) {
                    return;
                }

                String item = cbxTitles.getSelectedItem().toString();
                String titles = tfdTitle.getText();

                cbxTitles.setSelectedIndex(-1);
                if (titles.contains(item)) {
                    return;
                }
                if (!titles.trim().isEmpty() && !(titles.endsWith(", ") || titles.endsWith(","))) {
                    titles += ", ";
                }
                titles += item;
                tfdTitle.setText(titles);
            }
        });
    }

    @Override
    public String getPanelName() {
        return LocaleManager.getString("personalData");
    }

    public List<Contact> getContacts() {
        return cmpContacts.getItems();
    }

    public Person.Sex getSex() {
        return rbtFemale.isSelected() ? Person.Sex.FEMALE : Person.Sex.MALE;
    }

    public Calendar getBirthDate() {
        Calendar birthDate = (Calendar) dchBirthDate.getCalendar().clone();

        birthDate.set(Calendar.HOUR_OF_DAY, 0);
        birthDate.set(Calendar.MINUTE, 0);
        birthDate.set(Calendar.SECOND, 0);
        birthDate.set(Calendar.MILLISECOND, 0);

        return birthDate;
    }

    public String getFirstName() {
        return tfdFirstName.getText();
    }

    public String getLastName() {
        return tfdLastName.getText();
    }

    private Address createAddress() {
        Address address = new Address();

        address.setState(LocaleManager.getString("State"));
        address.setDescription(LocaleManager.getString("home"));

        return address;
    }

    private void setAddresses(List<Address> addresses) {
        if (addresses.isEmpty()) {
            addresses.add(createAddress());
        }
        tbpAddresses.removeAll();

        for (Address address : addresses) {
            AddressPanel addressPanel = new AddressPanel(address);

            addressPanel.addDocumentListener(documentListener);
            tbpAddresses.addTab(address.getDescription(), addressPanel);
        }
    }

    private void updateAddresses(List<Address> addresses) {
        int index;

        for (index = 0; index < tbpAddresses.getTabCount(); index++) {
            AddressPanel addressPanel = (AddressPanel) tbpAddresses.getComponentAt(index);

            if (addresses.size() <= index) {
                addresses.add(new Address());
            }
            addresses.get(index).updateFrom(addressPanel.getAddress());
            addresses.get(index).setDescription(tbpAddresses.getTitleAt(index));
        }
        while (addresses.size() > index) {
            addresses.remove(addresses.size() - 1);
        }
    }

    @Override
    public void setupObject(Object object) {
        Person personObject = getNormalizedObject(Person.class, object);
        if (personObject == null) {
            return;
        }

        PrivatePersonData data = personObject.getPersonData(PrivatePersonData.class);
        personObject.setTitles(tfdTitle.getText());
        personObject.setFirstName(getFirstName());
        personObject.setLastName(getLastName());
        personObject.setSex(getSex());
        data.setBirthDate(getBirthDate());
        data.setContacts(getContacts());
        data.setPhoto(ipnPhoto.getImage());
        updateAddresses(data.getAddresses());
    }

    @Override
    public void setupPanel(Object object) {
        person = getNormalizedObject(Person.class, object);
        if (person == null) {
            return;
        }

        initialized = false;
    }
}
