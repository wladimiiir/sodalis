
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.folkensemble.member.ui;

import sk.magiksoft.sodalis.core.action.GoToEntityAction;
import sk.magiksoft.sodalis.core.factory.ColorList;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel;
import sk.magiksoft.sodalis.folkensemble.member.data.MemberDataManager;
import sk.magiksoft.sodalis.folkensemble.repertory.RepertoryModule;
import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song;
import sk.magiksoft.sodalis.folkensemble.repertory.ui.SongTableModel;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.swing.ISTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author wladimiiir
 */
public class MemberSongInfoPanel extends AbstractInfoPanel {

    private SongTableModel songTableModel = new SongTableModel();
    private Person person;
    private GoToEntityAction goToSongAction = new GoToEntityAction(RepertoryModule.class);

    public MemberSongInfoPanel() {
    }

    @Override public boolean isWizardSupported() {
        return false;
    }

    @Override
    protected Component createLayout() {
        final JPanel layoutPanel = new JPanel(new BorderLayout());
        final JTable table = new ISTable(songTableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.getViewport().setBackground(ColorList.SCROLLPANE_BACKGROUND);
        layoutPanel.add(scrollPane, BorderLayout.CENTER);

        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int row;
                Song song;

                if (e.getClickCount() != 2
                        || (row = table.rowAtPoint(e.getPoint())) == -1) {
                    return;
                }
                song = songTableModel.getObject(row);
                goToSongAction.goTo(song);
            }
        });
        scrollPane.setPreferredSize(new Dimension(100, 50));

        return layoutPanel;
    }


    @Override
    public String getPanelName() {
        return LocaleManager.getString("interpretation");
    }

    @Override
    public void setupObject(Object object) {
    }

    @Override
    public void setupPanel(Object object) {
        if (!(object instanceof Person)) {
            return;
        }

        this.person = (Person) object;
        initialized = false;
    }

    @Override
    public void initData() {
        if (initialized || person == null) {
            return;
        }

        songTableModel.setObjects(MemberDataManager.getDatabaseEntities(
                "select s from Song s, PersonWrapper pw where pw.person.id=" + person.getId() + " and pw in elements(s.interpreters)"));
        initialized = true;
    }


}