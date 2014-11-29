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
import java.util.*;

/**
 * @author wladimiiir
 */
public class MemberSongInfoPanel extends AbstractInfoPanel {

    private SongTableModel songTableModel = new SongTableModel();
    private Person person;
    private GoToEntityAction goToSongAction = new GoToEntityAction(RepertoryModule.class);

    public MemberSongInfoPanel() {
    }

    @Override
    public boolean isWizardSupported() {
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

        final java.util.List<?> databaseEntities = MemberDataManager.getDatabaseEntities(
                "select s from Song s, PersonWrapper pw where pw.person.id=" + person.getId() + " and pw in elements(s.interpreters)");
        final java.util.List<Song> songs = new ArrayList<>();
        for (Object databaseEntity : databaseEntities) {
            if(databaseEntity instanceof Song) {
                songs.add((Song) databaseEntity);
            }
        }
        songTableModel.setObjects(songs);
        initialized = true;
    }


}
