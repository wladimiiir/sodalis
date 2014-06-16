
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.person.ui;

import sk.magiksoft.sodalis.core.enumeration.EnumerationEntry;
import sk.magiksoft.sodalis.core.enumeration.EnumerationFactory;
import sk.magiksoft.sodalis.core.enumeration.EnumerationList;
import sk.magiksoft.sodalis.core.enumeration.Enumerations;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.person.entity.Address;
import sk.magiksoft.sodalis.person.entity.CityEnumerationEntry;
import sk.magiksoft.swing.CheckedTextField;
import sk.magiksoft.swing.ListTextField;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author wladimiiir
 */
public class AddressPanel extends JPanel {

    private JTextField tfdNumber;
    private JTextField tfdPostcode;
    private JTextField tfdState;
    private JTextField tfdStreet;
    private ListTextField<EnumerationEntry> tfdTown;

    public AddressPanel() {
        initComponents();
    }

    public AddressPanel(Address address) {
        this();
        setAddress(address);
    }

    public void addDocumentListener(DocumentListener listener) {
        tfdNumber.getDocument().addDocumentListener(listener);
        tfdPostcode.getDocument().addDocumentListener(listener);
        tfdState.getDocument().addDocumentListener(listener);
        tfdStreet.getDocument().addDocumentListener(listener);
        tfdTown.getDocument().addDocumentListener(listener);
    }

    private void initComponents() {
        GridBagConstraints c;

        tfdStreet = new JTextField();
        tfdNumber = new CheckedTextField("\\d+[/\\\\]?\\d*");
        tfdTown = new ListTextField<EnumerationEntry>(new EnumerationList(EnumerationFactory.getInstance().getEnumeration(Enumerations.CITY)));
        tfdPostcode = new CheckedTextField("\\d{0,5}");
        tfdState = new JTextField();

        setLayout(new GridBagLayout());

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, 5, 3, 0);
        add(new JLabel(LocaleManager.getString("street")), c);

        tfdStreet.setPreferredSize(new Dimension(150, 21));
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(0, 2, 2, 0);
        add(tfdStreet, c);

        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, 5, 2, 0);
        add(new JLabel(LocaleManager.getString("number")), c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, 5, 3, 0);
        add(new JLabel(LocaleManager.getString("town")), c);

        tfdNumber.setMinimumSize(new Dimension(60, 22));
        tfdNumber.setPreferredSize(new Dimension(60, 21));
        c = new GridBagConstraints();
        c.gridx = 3;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 2, 2, 5);
        add(tfdNumber, c);

        tfdTown.setPreferredSize(new Dimension(150, 21));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(0, 2, 2, 0);
        add(tfdTown, c);

        tfdPostcode.setMinimumSize(new Dimension(60, 22));
        tfdPostcode.setPreferredSize(new Dimension(60, 21));
        c = new GridBagConstraints();
        c.gridx = 3;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 2, 2, 5);
        add(tfdPostcode, c);

        tfdState.setPreferredSize(new Dimension(150, 21));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(0, 2, 2, 0);
        add(tfdState, c);

        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 1;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, 5, 2, 0);
        add(new JLabel(LocaleManager.getString("postcode")), c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, 5, 3, 0);
        add(new JLabel(LocaleManager.getString("state")), c);

        tfdTown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final EnumerationEntry currentObject = tfdTown.getCurrentObject();
                if (currentObject instanceof CityEnumerationEntry) {
                    tfdPostcode.setText(((CityEnumerationEntry) currentObject).getZipCode());
                }
            }
        });
    }

    public void setAddress(Address address) {
        tfdStreet.setText(address.getStreet());
        tfdNumber.setText(address.getNumber());
        tfdPostcode.setText(address.getPostcode());
        tfdState.setText(address.getState());
        tfdTown.setText(address.getTown());
    }

    public Address getAddress() {
        Address address = new Address();

        address.setNumber(tfdNumber.getText());
        address.setPostcode(tfdPostcode.getText());
        address.setState(tfdState.getText());
        address.setStreet(tfdStreet.getText());
        address.setTown(tfdTown.getText());

        return address;
    }

}