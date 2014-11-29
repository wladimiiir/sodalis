package sk.magiksoft.sodalis.folkensemble.repertory.ui;

import sk.magiksoft.sodalis.core.data.ComboBoxDataManager;
import sk.magiksoft.sodalis.core.enumeration.Enumerations;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel;
import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song;
import sk.magiksoft.sodalis.person.entity.ManagerData;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.sodalis.person.ui.PersonWrapperTextFieldsPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

/**
 * @author wladimiiir
 */
public class SongInfoPanel extends AbstractInfoPanel {

    private Song song;
    private JSpinner spnDuration;
    private JComboBox cbxRegion;
    private JComboBox cbxGenre;
    private JPanel panel1;
    private JPanel panel2;
    private JPanel pnlBasic;
    private JPanel pnlMiddle;
    private JScrollPane spnDescription;
    private JTextField tfdSongName;
    private JTextArea txaDescription;
    private PersonWrapperTextFieldsPanel pwtfpComposers;
    private PersonWrapperTextFieldsPanel pwtfpChoreographers;
    private PersonWrapperTextFieldsPanel pwtfpPedagogists;

    @Override
    protected Component createLayout() {
        final JPanel layoutPanel = new JPanel(new GridLayout(1, 2));
        GridBagConstraints c;
        JLabel jLabel1 = new JLabel();
        JLabel jLabel2 = new JLabel();
        JLabel jLabel3 = new JLabel();
        JLabel jLabel4 = new JLabel();
        JLabel jLabel5 = new JLabel();

        pnlBasic = new JPanel();
        panel1 = new JPanel();
        tfdSongName = new JTextField();
        spnDescription = new JScrollPane();
        txaDescription = new JTextArea();
        cbxRegion = new JComboBox();
        cbxGenre = new JComboBox();
        spnDuration = new JSpinner(new SpinnerDateModel());
        panel2 = new JPanel();
        pnlMiddle = new JPanel();
        pwtfpChoreographers = new ManagerTextFieldsPanel();
        pwtfpComposers = new ManagerTextFieldsPanel();
        pwtfpPedagogists = new ManagerTextFieldsPanel();


        cbxRegion.setEditable(true);
        cbxGenre.setEditable(true);
        spnDuration.setEditor(new JSpinner.DateEditor(spnDuration, "m:ss"));
        spnDuration.setModel(new SpinnerDateModel());
        ((JSpinner.DateEditor) spnDuration.getEditor()).getTextField().setHorizontalAlignment(
                SwingConstants.RIGHT);
        ComboBoxDataManager.getInstance().registerComboBox(Enumerations.SONG_GENRE, cbxGenre);
        ComboBoxDataManager.getInstance().registerComboBox(Enumerations.FOLK_REGION, cbxRegion);

        pnlBasic.setBorder(BorderFactory.createTitledBorder(LocaleManager.getString("basicInfo")));
        pnlBasic.setLayout(new GridBagLayout());

        panel1.setLayout(new GridBagLayout());

        jLabel1.setText(LocaleManager.getString("songName"));
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(5, 10, 0, 5);
        panel1.add(jLabel1, c);

        jLabel2.setText(LocaleManager.getString("description"));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.insets = new Insets(5, 5, 0, 5);
        panel1.add(jLabel2, c);

        jLabel3.setText(LocaleManager.getString("songGenre"));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(5, 10, 0, 5);
        panel1.add(jLabel3, c);

        jLabel4.setText(LocaleManager.getString("region"));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(5, 10, 0, 5);
        panel1.add(jLabel4, c);

        jLabel5.setText(LocaleManager.getString("songDuration"));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(5, 10, 0, 5);
        panel1.add(jLabel5, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;
        c.insets = new Insets(5, 0, 0, 10);
        panel1.add(tfdSongName, c);

        spnDescription.setMinimumSize(new Dimension(22, 78));

        txaDescription.setColumns(10);
        txaDescription.setRows(5);
        spnDescription.setViewportView(txaDescription);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(5, 0, 0, 10);
        panel1.add(spnDescription, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;
        c.insets = new Insets(5, 0, 0, 10);
        panel1.add(cbxRegion, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;
        c.insets = new Insets(5, 0, 0, 10);
        panel1.add(cbxGenre, c);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;
        c.insets = new Insets(5, 0, 0, 10);
        panel1.add(spnDuration, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        c.weightx = 1.0;
        c.insets = new Insets(5, 5, 5, 5);
        pnlBasic.add(panel1, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        pnlBasic.add(panel2, c);

        layoutPanel.add(pnlBasic);

        pnlMiddle.setLayout(new GridLayout(3, 1));

        JScrollPane scrollPane = new JScrollPane(pwtfpComposers);
        scrollPane.setBorder(BorderFactory.createTitledBorder(LocaleManager.getString("musicComposing")));
        scrollPane.setMaximumSize(new Dimension(1000, 60));
        scrollPane.setPreferredSize(new Dimension(100, 60));
        pnlMiddle.add(scrollPane);

        scrollPane = new JScrollPane(pwtfpChoreographers);
        scrollPane.setBorder(BorderFactory.createTitledBorder(LocaleManager.getString("choreography")));
        scrollPane.setMaximumSize(new Dimension(1000, 60));
        scrollPane.setPreferredSize(new Dimension(100, 60));
        pnlMiddle.add(scrollPane);

        scrollPane = new JScrollPane(pwtfpPedagogists);
        scrollPane.setBorder(BorderFactory.createTitledBorder(LocaleManager.getString("pedagogists")));
        scrollPane.setMaximumSize(new Dimension(1000, 60));
        scrollPane.setPreferredSize(new Dimension(100, 60));
        pnlMiddle.add(scrollPane);

        layoutPanel.add(pnlMiddle);
        initListeners();

        return layoutPanel;
    }

    @Override
    public String getPanelName() {
        return LocaleManager.getString("song");
    }

    @Override
    public void setupObject(Object object) {
        if (!(object instanceof Song)) {
            return;
        }
        final Song s = (Song) object;
        Calendar c = Calendar.getInstance();

        c.setTime(((SpinnerDateModel) spnDuration.getModel()).getDate());

        s.setName(tfdSongName.getText());
        s.setDescription(txaDescription.getText());
        s.setGenre(cbxGenre.getEditor().getItem().toString());
        s.setRegion(cbxRegion.getEditor().getItem().toString());
        s.setDuration(c.get(Calendar.MINUTE) * 60 + c.get(Calendar.SECOND));
        s.setComposers(pwtfpComposers.getValues());
        s.setChoreographers(pwtfpChoreographers.getValues());
        s.setPedagogists(pwtfpPedagogists.getValues());
    }

    @Override
    public void setupPanel(Object object) {
        if (!(object instanceof Song)) {
            return;
        }

        song = (Song) object;
        initialized = false;
    }

    @Override
    public void initData() {
        if (song == null || initialized) {
            return;
        }

        tfdSongName.setText(song.getName());
        txaDescription.setText(song.getDescription());
        cbxGenre.getEditor().setItem(song.getGenre());
        cbxRegion.getEditor().setItem(song.getRegion());
        spnDuration.setValue(new Date(song.getDuration() * 1000));
        pwtfpComposers.setValues(song.getComposers());
        pwtfpChoreographers.setValues(song.getChoreographers());
        pwtfpPedagogists.setValues(song.getPedagogists());

        initialized = true;
    }

    private void initListeners() {
        tfdSongName.getDocument().addDocumentListener(documentListener);
        txaDescription.getDocument().addDocumentListener(documentListener);
        cbxGenre.addItemListener(itemListener);
        cbxRegion.addItemListener(itemListener);
        spnDuration.addChangeListener(changeListener);
        pwtfpChoreographers.addChangeListener(changeListener);
        pwtfpComposers.addChangeListener(changeListener);
        pwtfpPedagogists.addChangeListener(changeListener);
    }

    private class ManagerTextFieldsPanel extends PersonWrapperTextFieldsPanel {

        @Override
        protected String getPersonQuery() {
            return "select p from Person p, ManagerData md where md in elements(p.personDatas)";
        }

        @Override
        protected boolean acceptObject(Object object) {
            if (!(object instanceof Person)) {
                return false;
            }

            Person person = (Person) object;

            return person.getPersonData(ManagerData.class) != null;
        }
    }
}
