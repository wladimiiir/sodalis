package sk.magiksoft.sodalis.folkensemble.repertory.action;

import sk.magiksoft.sodalis.core.action.ActionMessage;
import sk.magiksoft.sodalis.core.action.MessageAction;
import sk.magiksoft.sodalis.core.factory.IconFactory;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;
import sk.magiksoft.sodalis.folkensemble.repertory.data.RepertoryDataManager;
import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class RemoveSongAction extends MessageAction {

    private List<Song> songs;

    public RemoveSongAction() {
        super("", IconFactory.getInstance().getIcon("remove"));
    }

    @Override
    public ActionMessage getActionMessage(List objects) {
        songs = getAcceptedSongs(objects);
        return songs.isEmpty() ? new ActionMessage(false, LocaleManager.getString("noSongSelected"))
                : new ActionMessage(true, LocaleManager.getString("removeSong"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (songs == null || songs.isEmpty()) {
            return;
        }

        int result;
        if (songs.size() == 1) {
            result = ISOptionPane.showConfirmDialog(null, LocaleManager.getString("removeSongConfirm"), songs.get(0).getName(), JOptionPane.YES_NO_OPTION);
        } else {
            result = ISOptionPane.showConfirmDialog(null, LocaleManager.getString("removeSongsConfirm"), "", JOptionPane.YES_NO_OPTION);
        }

        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        for (Song song : songs) {
            RepertoryDataManager.getInstance().removeDatabaseEntity(song);
        }
    }

    private List<Song> getAcceptedSongs(List objects) {
        List<Song> accepted = new ArrayList<Song>();

        if (objects == null) {
            return accepted;
        }
        for (Object object : objects) {
            if (object instanceof Song) {
                accepted.add((Song) object);
            }
        }

        return accepted;
    }
}
