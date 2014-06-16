
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EmailServerInfoPanel.java
 *
 * Created on 24.8.2009, 18:10:49
 */
package sk.magiksoft.sodalis.person.ui;

import sk.magiksoft.sodalis.core.entity.EmailServer;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.ui.Validator;
import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel;
import sk.magiksoft.sodalis.person.entity.InternetData;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.sodalis.person.utils.PersonUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.regex.Matcher;

/**
 * @author wladimiiir
 */
public class EmailServerInfoPanel extends AbstractInfoPanel {

    private Person person;
    private Validator emailValidator;

    /**
     * Creates new form EmailServerInfoPanel
     */
    public EmailServerInfoPanel() {
        super(Person.class);
    }

    @Override
    protected Component createLayout() {
        final JPanel layoutPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        tfdEmail = new JTextField();
        tfdFullname = new JTextField();
        jPanel2 = new JPanel();
        jLabel3 = new JLabel();
        tfdServer = new JTextField();
        chbRequiresLogin = new JCheckBox();
        jLabel4 = new JLabel();
        tfdLogin = new JTextField();
        chbRememberPassword = new JCheckBox();
        pfdPassword = new JPasswordField();
        pfdPasswordAgain = new JPasswordField();
        jLabel5 = new JLabel();
        jLabel6 = new JLabel();

        layoutPanel.setName("Form"); // NOI18N

        jPanel1.setBorder(BorderFactory.createTitledBorder(LocaleManager.getString("identity")));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new GridBagLayout());

        jLabel1.setText(LocaleManager.getString("email"));
        jLabel1.setName("jLabel1"); // NOI18N
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(10, 10, 0, 0);
        jPanel1.add(jLabel1, c);

        jLabel2.setText(LocaleManager.getString("fullname"));
        jLabel2.setName("jLabel2"); // NOI18N
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(2, 10, 0, 0);
        jPanel1.add(jLabel2, c);

        tfdEmail.setName("tfdEmail"); // NOI18N
        tfdEmail.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                tfdEmailFocusLost(evt);
            }
        });
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(10, 3, 0, 10);
        jPanel1.add(tfdEmail, c);

        tfdFullname.setName("tfdFullname"); // NOI18N
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(2, 3, 0, 10);
        jPanel1.add(tfdFullname, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTH;
        c.weightx = 1.0;
        layoutPanel.add(jPanel1, c);

        jPanel2.setBorder(BorderFactory.createTitledBorder(LocaleManager.getString("emailSending")));
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(new GridBagLayout());

        jLabel3.setText(LocaleManager.getString("server"));
        jLabel3.setName("jLabel3"); // NOI18N
        c = new GridBagConstraints();
        c.insets = new Insets(2, 10, 0, 0);
        jPanel2.add(jLabel3, c);

        tfdServer.setName("tfdServer"); // NOI18N
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(2, 3, 0, 10);
        jPanel2.add(tfdServer, c);

        chbRequiresLogin.setSelected(true);
        chbRequiresLogin.setText(LocaleManager.getString("serverRequiresLogin"));
        chbRequiresLogin.setName("chbRequiresLogin"); // NOI18N
        chbRequiresLogin.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                chbRequiresLoginItemStateChanged(evt);
            }
        });
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(2, 10, 0, 0);
        jPanel2.add(chbRequiresLogin, c);

        jLabel4.setText(LocaleManager.getString("loginName"));
        jLabel4.setName("jLabel4"); // NOI18N
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(7, 10, 0, 0);
        jPanel2.add(jLabel4, c);

        tfdLogin.setName("tfdLogin"); // NOI18N
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(7, 3, 0, 10);
        jPanel2.add(tfdLogin, c);

        chbRememberPassword.setText(LocaleManager.getString("savePassword"));
        chbRememberPassword.setName("chbRememberPassword"); // NOI18N
        chbRememberPassword.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                chbRememberPasswordItemStateChanged(evt);
            }
        });
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(2, 10, 0, 0);
        jPanel2.add(chbRememberPassword, c);

        pfdPassword.setEnabled(false);
        pfdPassword.setName("pfdPassword"); // NOI18N
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(2, 3, 0, 10);
        jPanel2.add(pfdPassword, c);

        pfdPasswordAgain.setEnabled(false);
        pfdPasswordAgain.setName("pfdPasswordAgain"); // NOI18N
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(2, 3, 10, 10);
        jPanel2.add(pfdPasswordAgain, c);

        jLabel5.setText(LocaleManager.getString("password"));
        jLabel5.setEnabled(false);
        jLabel5.setName("jLabel5"); // NOI18N
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(2, 10, 0, 0);
        jPanel2.add(jLabel5, c);

        jLabel6.setText(LocaleManager.getString("passwordAgain"));
        jLabel6.setEnabled(false);
        jLabel6.setName("jLabel6"); // NOI18N
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 5;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(2, 10, 10, 0);
        jPanel2.add(jLabel6, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        layoutPanel.add(jPanel2, c);

        initComponents2();

        return layoutPanel;
    }

    private void initComponents2() {
        chbRememberPassword.setVisible(false);
        pfdPassword.setVisible(false);
        pfdPasswordAgain.setVisible(false);
        jLabel5.setVisible(false);
        jLabel6.setVisible(false);

        emailValidator = new Validator(tfdEmail) {

            @Override
            public boolean isValid(Object object) {
                final boolean valid = tfdEmail.getText().trim().isEmpty() || tfdEmail.getText().matches(PersonUtils.EMAIL_PATTERN.pattern());

                fireValid(valid);
                return valid;
            }
        };

        tfdEmail.getDocument().addDocumentListener(documentListener);
        tfdFullname.getDocument().addDocumentListener(documentListener);
        tfdLogin.getDocument().addDocumentListener(documentListener);
        tfdServer.getDocument().addDocumentListener(documentListener);
        chbRequiresLogin.addItemListener(itemListener);
    }

    private void chbRequiresLoginItemStateChanged(ItemEvent evt) {//GEN-FIRST:event_chbRequiresLoginItemStateChanged
        refreshEnability();
    }//GEN-LAST:event_chbRequiresLoginItemStateChanged

    private void chbRememberPasswordItemStateChanged(ItemEvent evt) {//GEN-FIRST:event_chbRememberPasswordItemStateChanged
        refreshEnability();
    }//GEN-LAST:event_chbRememberPasswordItemStateChanged

    private void tfdEmailFocusLost(FocusEvent evt) {//GEN-FIRST:event_tfdEmailFocusLost
        Matcher matcher = PersonUtils.EMAIL_PATTERN.matcher(tfdEmail.getText());

        if (tfdEmail.getText().trim().isEmpty()) {
            tfdServer.setText("");
            tfdLogin.setText("");
        }
        if (!matcher.matches()) {
            return;
        }
        String username = matcher.group(1);
        String server = matcher.group(2);

        if (tfdServer.getText().trim().isEmpty()) {
            tfdServer.setText("smtp." + server);
        }
        if (tfdLogin.getText().trim().isEmpty()) {
            tfdLogin.setText(username);
        }

    }//GEN-LAST:event_tfdEmailFocusLost

    private void refreshEnability() {
        tfdLogin.setEnabled(chbRequiresLogin.isSelected());
        chbRememberPassword.setEnabled(chbRequiresLogin.isSelected());
        pfdPassword.setEnabled(chbRequiresLogin.isSelected() && chbRememberPassword.isSelected());
        pfdPasswordAgain.setEnabled(chbRequiresLogin.isSelected() && chbRememberPassword.isSelected());
    }

    @Override
    public String getPanelName() {
        return LocaleManager.getString("emailServer");
    }

    @Override
    public void setupObject(Object object) {
        if (!acceptObject(object)) {
            return;
        }
        Person person = getNormalizedObject(Person.class, object);
        InternetData data = person.getPersonData(InternetData.class);

        if (data.getEmailServers().isEmpty()) {
            data.getEmailServers().add(new EmailServer());
        }

        for (EmailServer emailServer : data.getEmailServers()) {
            emailServer.setEmailAddress(tfdEmail.getText());
            emailServer.setFullName(tfdFullname.getText());
            emailServer.setHostname(tfdServer.getText());
            emailServer.setUsername(chbRequiresLogin.isSelected() ? tfdLogin.getText() : null);

//            if(pfdPassword.getPassword().length>0){
//                setPassword(emailServer);
//            }
        }

    }

    private void setPassword(EmailServer emailServer) {
        //TODO: later when password persisting is resolved
    }

    @Override
    public void setupPanel(Object object) {
        if (!acceptObject(object)) {
            return;
        }
        person = getNormalizedObject(Person.class, object);

        initialized = false;
    }

    @Override
    public void initData() {
        InternetData data = person.getPersonData(InternetData.class);
        EmailServer emailServer = data.getEmailServers().isEmpty() ? null : data.getEmailServers().get(0);

        if (emailServer == null) {
            tfdEmail.setText("");
            tfdFullname.setText("");
            tfdServer.setText("");
            tfdLogin.setText("");
        } else {
            tfdEmail.setText(emailServer.getEmailAddress());
            tfdFullname.setText(emailServer.getFullName());
            tfdServer.setText(emailServer.getHostname());
            tfdLogin.setText(emailServer.getUsername());
        }

        initialized = true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JCheckBox chbRememberPassword;
    private JCheckBox chbRequiresLogin;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPasswordField pfdPassword;
    private JPasswordField pfdPasswordAgain;
    private JTextField tfdEmail;
    private JTextField tfdFullname;
    private JTextField tfdLogin;
    private JTextField tfdServer;
    // End of variables declaration//GEN-END:variables
}