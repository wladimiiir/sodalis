package sk.magiksoft.sodalis.folkensemble.member.ui;

import sk.magiksoft.sodalis.core.enumeration.EnumerationDataModel;
import sk.magiksoft.sodalis.core.enumeration.EnumerationFactory;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel;
import sk.magiksoft.sodalis.core.utils.Utils;
import sk.magiksoft.sodalis.folkensemble.member.entity.EnsembleData;
import sk.magiksoft.sodalis.folkensemble.member.entity.EnsembleGroup;
import sk.magiksoft.sodalis.folkensemble.member.entity.EnsembleMemberHistory;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.swing.MultiSelectComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class EnsembleDataPanel extends AbstractInfoPanel {
    private static final String MUSIC_INSTRUMENT_ENUMERATION = "MusicInstrument";

    private javax.swing.JCheckBox chbDancer;
    private javax.swing.JCheckBox chbMusician;
    private javax.swing.JCheckBox chbSinger;
    private MultiSelectComboBox mcbDancerType;
    private MultiSelectComboBox mcbSingerType;
    private MultiSelectComboBox mcbMusicianInstrument;
    private javax.swing.JPanel pnlGroup;
    private javax.swing.JPanel pnlHistory;
    private FolkEnsembleHistoryComponent folkEnsembleHistory;
    private Person ensembleMember;


    @Override
    public void initData() {
        if (initialized) {
            return;
        }
        EnsembleGroup group = ensembleMember.getPersonData(EnsembleData.class).getEnsembleGroup();
        chbDancer.setSelected(group.isGroupType(EnsembleGroup.GROUP_TYPE_DANCER));
        chbSinger.setSelected(group.isGroupType(EnsembleGroup.GROUP_TYPE_SINGER));
        chbMusician.setSelected(group.isGroupType(EnsembleGroup.GROUP_TYPE_MUSICIAN));

        refreshEnability();
        mcbDancerType.setSelectedObjects(group.getGroupInfo(EnsembleGroup.GROUPINFO_DANCER_TYPE) == null ? new ArrayList() : (List) group.getGroupInfo(EnsembleGroup.GROUPINFO_DANCER_TYPE));
        mcbSingerType.setSelectedObjects(group.getGroupInfo(EnsembleGroup.GROUPINFO_SINGER_TYPE) == null ? new ArrayList() : (List) group.getGroupInfo(EnsembleGroup.GROUPINFO_SINGER_TYPE));
        mcbMusicianInstrument.setSelectedObjects(group.getGroupInfo(EnsembleGroup.GROUPINFO_MUSICIAN_INSTRUMENT) == null ? new ArrayList() : (List) group.getGroupInfo(EnsembleGroup.GROUPINFO_MUSICIAN_INSTRUMENT));
        folkEnsembleHistory.setItems(Utils.cloneItems(ensembleMember.getPersonData(EnsembleData.class).getMemberHistories()));
        initialized = true;
    }

    @Override
    protected Component createLayout() {
        final JPanel layoutPanel = new JPanel(new GridBagLayout());
        java.awt.GridBagConstraints c;

        pnlGroup = new javax.swing.JPanel();
        chbDancer = new javax.swing.JCheckBox();
        chbSinger = new javax.swing.JCheckBox();
        chbMusician = new javax.swing.JCheckBox();
        pnlHistory = new javax.swing.JPanel();
        folkEnsembleHistory = new FolkEnsembleHistoryComponent();
        mcbDancerType = new MultiSelectComboBox();
        mcbSingerType = new MultiSelectComboBox();
        mcbMusicianInstrument = new MultiSelectComboBox(new EnumerationDataModel(EnumerationFactory.getInstance().getEnumeration(MUSIC_INSTRUMENT_ENUMERATION)));

        mcbDancerType.addItem(LocaleManager.getString("solo"));
        mcbDancerType.addItem(LocaleManager.getString("group"));

        mcbSingerType.addItem(LocaleManager.getString("solo"));
        mcbSingerType.addItem(LocaleManager.getString("group"));

        layoutPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(LocaleManager.getString("EnsembleData")));

        pnlGroup.setBorder(javax.swing.BorderFactory.createTitledBorder(LocaleManager.getString("EnsembleGroup.name")));
        pnlGroup.setLayout(new java.awt.GridLayout(2, 0, 10, 5));

        chbDancer.setText(LocaleManager.getString("EnsembleGroup.dancer"));
        pnlGroup.add(chbDancer);

        chbSinger.setText(LocaleManager.getString("EnsembleGroup.singer"));
        pnlGroup.add(chbSinger);

        chbMusician.setText(LocaleManager.getString("EnsembleGroup.musician"));
        pnlGroup.add(chbMusician);

        //second row
        pnlGroup.add(mcbDancerType);
        pnlGroup.add(mcbSingerType);
        pnlGroup.add(mcbMusicianInstrument);

        c = new java.awt.GridBagConstraints();
        c.fill = java.awt.GridBagConstraints.BOTH;
        c.weightx = 1.0;
        layoutPanel.add(pnlGroup, c);

        pnlHistory.setBorder(javax.swing.BorderFactory.createTitledBorder(LocaleManager.getString("FolkEnsembleHistory")));
        pnlHistory.setLayout(new BorderLayout());
        pnlHistory.add(folkEnsembleHistory);
        pnlHistory.setPreferredSize(new Dimension(100, 50));

        c = new java.awt.GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.fill = java.awt.GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 2.0;
        layoutPanel.add(pnlHistory, c);

        setupListeners();
        refreshEnability();

        return layoutPanel;
    }

    private void setupListeners() {
        ActionListener actionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                refreshEnability();
                fireEditing();
            }
        };

        chbDancer.addActionListener(actionListener);
        chbMusician.addActionListener(actionListener);
        chbSinger.addActionListener(actionListener);
        folkEnsembleHistory.addItemComponentListener(itemComponentListener);
        mcbDancerType.addChangeListener(changeListener);
        mcbSingerType.addChangeListener(changeListener);
        mcbMusicianInstrument.addChangeListener(changeListener);
    }

    private void refreshEnability() {
        mcbMusicianInstrument.setEnabled(chbMusician.isSelected());
        mcbSingerType.setEnabled(chbSinger.isSelected());
        mcbDancerType.setEnabled(chbDancer.isSelected());
    }

    @Override
    public String getPanelName() {
        return LocaleManager.getString("EnsembleData");
    }

    public EnsembleGroup getGroup() {
        int groupType = 0;
        if (chbDancer.isSelected()) {
            groupType += EnsembleGroup.GROUP_TYPE_DANCER;
        }
        if (chbSinger.isSelected()) {
            groupType += EnsembleGroup.GROUP_TYPE_SINGER;
        }
        if (chbMusician.isSelected()) {
            groupType += EnsembleGroup.GROUP_TYPE_MUSICIAN;
        }
        EnsembleGroup group = new EnsembleGroup(groupType);

        group.putGroupInfo(EnsembleGroup.GROUPINFO_DANCER_TYPE, new ArrayList(mcbDancerType.getSelectedObjects()));
        group.putGroupInfo(EnsembleGroup.GROUPINFO_SINGER_TYPE, new ArrayList(mcbSingerType.getSelectedObjects()));
        group.putGroupInfo(EnsembleGroup.GROUPINFO_MUSICIAN_INSTRUMENT, new ArrayList(mcbMusicianInstrument.getSelectedObjects()));

        return group;
    }

    public List<EnsembleMemberHistory> getMemberHistories() {
        return folkEnsembleHistory.getItems();
    }

    @Override
    public void setupObject(Object object) {
        if (!(object instanceof Person) || ((Person) object).getPersonData(EnsembleData.class) == null) {
            return;
        }
        Person person = (Person) object;
        person.getPersonData(EnsembleData.class).getEnsembleGroup().updateFrom(getGroup());
        person.getPersonData(EnsembleData.class).setMemberHistories(getMemberHistories());
    }

    @Override
    public void setupPanel(Object object) {
        if (!(object instanceof Person) || ((Person) object).getPersonData(EnsembleData.class) == null) {
            return;
        }
        ensembleMember = (Person) object;
        initialized = false;
    }
}
