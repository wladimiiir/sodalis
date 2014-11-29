package sk.magiksoft.sodalis.folkensemble.programme.ui;

import sk.magiksoft.sodalis.core.action.ContextTransferAction;
import sk.magiksoft.sodalis.core.action.GoToEntityAction;
import sk.magiksoft.sodalis.core.context.Context;
import sk.magiksoft.sodalis.core.factory.ColorList;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;
import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel;
import sk.magiksoft.sodalis.folkensemble.programme.ProgrammeModule;
import sk.magiksoft.sodalis.folkensemble.programme.entity.Programme;
import sk.magiksoft.sodalis.folkensemble.programme.entity.ProgrammeSong;
import sk.magiksoft.sodalis.folkensemble.repertory.RepertoryModule;
import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song;
import sk.magiksoft.sodalis.person.entity.PersonWrapper;
import sk.magiksoft.sodalis.person.utils.PersonUtils;
import sk.magiksoft.swing.ISTable;
import sk.magiksoft.swing.table.MultiselectComboBoxTableCellEditor;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class ProgrammeSongInfoPanel extends AbstractInfoPanel {

    private ProgrammeSongTableModel programmeSongTableModel = new ProgrammeSongTableModel();
    private Programme programme;
    private GoToEntityAction goToSongAction = new GoToEntityAction(RepertoryModule.class);
    private JButton btnAddSongs = new JButton(new AddSongsAction());
    private JButton btnRemoveSongs = new JButton(new RemoveSongsAction());
    private final List<AbstractButton> controlPanelButtons = new ArrayList<AbstractButton>(Arrays.asList(btnAddSongs, btnRemoveSongs));

    public List<ProgrammeSong> getProgrammeSongs() {
        return new ArrayList<ProgrammeSong>(programmeSongTableModel.getObjects());
    }

    public void setProgrammeSongs(List<ProgrammeSong> programmeSongs) {
        programmeSongTableModel.setObjects(programmeSongs);
    }

    @Override
    protected Component createLayout() {
        final JPanel layoutPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        scrollPane = new JScrollPane();
        tblProgrammeSong = new ISTable();
        tblProgrammeSong.removeSortFunction();
        totalSongDurationScrollPane = new JScrollPane();
        tblTotalSongDuration = new JTable();
        pnlMoveUpDown = new JPanel();
        btnMoveUp = new JButton();
        btnMoveDown = new JButton();
        pnlBottom = new JPanel();

        scrollPane.getViewport().setBackground(ColorList.SCROLLPANE_BACKGROUND);

        tblProgrammeSong.setModel(programmeSongTableModel);
        tblProgrammeSong.setPreferredScrollableViewportSize(new Dimension(450, 100));
        tblProgrammeSong.getColumnModel().getColumn(2).setCellEditor(new ProgrammeSongInfoPanel.InterpretersTableCellEditor());
        tblProgrammeSong.getColumnModel().getColumn(2).setCellRenderer(new ProgrammeSongInfoPanel.InterpreterTableCellRenderer());

        tblProgrammeSong.getColumnModel().getColumn(0).setMinWidth(25);
        tblProgrammeSong.getColumnModel().getColumn(0).setMaxWidth(25);
        tblProgrammeSong.getColumnModel().getColumn(3).setMinWidth(60);
        tblProgrammeSong.getColumnModel().getColumn(3).setMaxWidth(60);
        tblProgrammeSong.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!SwingUtilities.isLeftMouseButton(e) || e.getClickCount() != 2) {
                    return;
                }
                final int row = tblProgrammeSong.rowAtPoint(e.getPoint());
                if (row == -1) {
                    return;
                }
                goToSongAction.goTo(programmeSongTableModel.getObject(row).getSong());
            }
        });
        scrollPane.setViewportView(tblProgrammeSong);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(3, 3, 0, 3);
        layoutPanel.add(scrollPane, c);

        totalSongDurationScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        totalSongDurationScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        totalSongDurationScrollPane.setMaximumSize(new Dimension(32767, 18));
        totalSongDurationScrollPane.setMinimumSize(new Dimension(18, 18));
        totalSongDurationScrollPane.setPreferredSize(new Dimension(0, 18));

        tblTotalSongDuration.setModel(new SongTotalTimeTableModel(programmeSongTableModel));
        tblTotalSongDuration.setFocusable(false);
        tblTotalSongDuration.setRowSelectionAllowed(false);
        tblTotalSongDuration.getTableHeader().setReorderingAllowed(false);
        tblTotalSongDuration.getColumnModel().getColumn(1).setMinWidth(60);
        tblTotalSongDuration.getColumnModel().getColumn(1).setMaxWidth(60);

        tblTotalSongDuration.getTableHeader().setPreferredSize(new Dimension(0, 0));
        totalSongDurationScrollPane.setViewportView(tblTotalSongDuration);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 3, 0, 3);
        layoutPanel.add(totalSongDurationScrollPane, c);

        pnlMoveUpDown.setLayout(new GridLayout(1, 0));

        btnMoveUp.setFont(new Font("Dialog", 1, 8));
        btnMoveUp.setText("▲"); // NOI18N
        btnMoveUp.setFocusPainted(false);
        btnMoveUp.setMaximumSize(new Dimension(88, 12));
        btnMoveUp.setMinimumSize(new Dimension(88, 12));
        btnMoveUp.setPreferredSize(new Dimension(88, 12));
        btnMoveUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnMoveUpActionPerformed(evt);
            }
        });
        pnlMoveUpDown.add(btnMoveUp);

        btnMoveDown.setFont(new Font("Dialog", 1, 8));
        btnMoveDown.setText("▼"); // NOI18N
        btnMoveDown.setFocusPainted(false);
        btnMoveDown.setMaximumSize(new Dimension(88, 12));
        btnMoveDown.setMinimumSize(new Dimension(88, 12));
        btnMoveDown.setPreferredSize(new Dimension(88, 12));
        btnMoveDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnMoveDownActionPerformed(evt);
            }
        });
        pnlMoveUpDown.add(btnMoveDown);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 3, 0, 3);
        layoutPanel.add(pnlMoveUpDown, c);

        btnRemoveSongs.setEnabled(false);
        initListeners();

        return layoutPanel;
    }

    @Override
    public List<AbstractButton> getControlPanelButtons() {
        return controlPanelButtons;
    }

    private void btnMoveUpActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnMoveUpActionPerformed
        int[] selected = tblProgrammeSong.getSelectedRows();

        Arrays.sort(selected);
        for (int i = 0; i < selected.length; i++) {
            int row = selected[i] - 1;
            if (row < 0 || Arrays.binarySearch(selected, row) > -1) {
                continue;
            }

            ProgrammeSong song = programmeSongTableModel.removeObject(row + 1);
            programmeSongTableModel.addObject(row, song);
            selected[i] = row;
        }
        programmeSongTableModel.fireTableDataChanged();
        for (int row : selected) {
            tblProgrammeSong.addRowSelectionInterval(row, row);
        }
    }//GEN-LAST:event_btnMoveUpActionPerformed

    private void btnMoveDownActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnMoveDownActionPerformed
        int[] selected = tblProgrammeSong.getSelectedRows();

        Arrays.sort(selected);
        for (int i = selected.length - 1; i >= 0; i--) {
            int row = selected[i] + 1;
            if (row > tblProgrammeSong.getRowCount() - 1 || Arrays.binarySearch(selected, row) > -1) {
                continue;
            }

            ProgrammeSong song = programmeSongTableModel.removeObject(row - 1);
            programmeSongTableModel.addObject(row, song);
            selected[i] = row;
        }
        programmeSongTableModel.fireTableDataChanged();
        for (int row : selected) {
            tblProgrammeSong.addRowSelectionInterval(row, row);
        }
    }//GEN-LAST:event_btnMoveDownActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnMoveDown;
    private JButton btnMoveUp;
    private JPanel pnlBottom;
    private JPanel pnlMoveUpDown;
    private JScrollPane scrollPane;
    private ISTable tblProgrammeSong;
    private JTable tblTotalSongDuration;
    private JScrollPane totalSongDurationScrollPane;
    // End of variables declaration//GEN-END:variables

    @Override
    public String getPanelName() {
        return LocaleManager.getString("songList");
    }

    @Override
    public void setupObject(Object object) {
        if (!(object instanceof Programme)) {
            return;
        }
        final Programme p = (Programme) object;

        p.setProgrammeSongs(programmeSongTableModel.getObjects());
    }

    @Override
    public void setupPanel(Object object) {
        if (!(object instanceof Programme)) {
            return;
        }

        programme = (Programme) object;
        initialized = false;
    }

    @Override
    public void initData() {
        if (programme == null || initialized) {
            return;
        }

        programmeSongTableModel.setObjects(programme.getProgrammeSongs());

        initialized = true;
    }

    private void initListeners() {
        programmeSongTableModel.addTableModelListener(tableModelListener);
        tblProgrammeSong.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                btnRemoveSongs.setEnabled(tblProgrammeSong.getSelectedRow() != -1);
            }
        });
    }
    // End of variables declaration

    private class InterpretersTableCellEditor extends MultiselectComboBoxTableCellEditor {

        @Override
        public boolean stopCellEditing() {
            programmeSongTableModel.fireTableDataChanged();
            return super.stopCellEditing();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            ProgrammeSong song = (ProgrammeSong) value;

            removeAllItems();
            for (PersonWrapper personWrapper : song.getSong().getInterpreters()) {
                addItem(new PersonWrapper(personWrapper));
            }
            setSelectedObjects(song.getInterpreters());
            return this;
        }
    }

    private class InterpreterTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (c instanceof JLabel && value instanceof ProgrammeSong) {
                ((JLabel) c).setText(PersonUtils.personWrappersToString(((ProgrammeSong) value).getInterpreters()));
            }

            return c;
        }
    }

    private class AddSongsAction extends ContextTransferAction {

        public AddSongsAction() {
            super(ProgrammeModule.class, RepertoryModule.class);
            putValue(Action.NAME, LocaleManager.getString("add"));
        }

        @Override
        protected boolean initialize(Context context) {
            List<Song> songs = new LinkedList<Song>();

            for (ProgrammeSong programmeSong : programmeSongTableModel.getObjects()) {
                songs.add(programmeSong.getSong());
            }

            context.setSelectedEntities(songs);

            return true;
        }

        @Override
        protected void finalize(Context context) {
            if (context == null) {
                return;
            }

            final List<Song> selectedSongs = context == null ? null : (List<Song>) context.getSelectedEntities();
            if (selectedSongs == null) {
                return;
            }

            final List<ProgrammeSong> programmeSongs = new ArrayList<ProgrammeSong>(selectedSongs.size());

            for (Song song : selectedSongs) {
                programmeSongs.add(new ProgrammeSong(song));
            }
            for (ProgrammeSong programmeSong : programmeSongs) {
                boolean found = false;
                for (ProgrammeSong modelProgrammeSong : programmeSongTableModel.getObjects()) {
                    if (programmeSong.getSong().getId().equals(modelProgrammeSong.getSong().getId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    programmeSongTableModel.addObject(programmeSong);
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        frame.add(new ProgrammeSongInfoPanel());
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private class RemoveSongsAction extends AbstractAction {
        private RemoveSongsAction() {
            super(LocaleManager.getString("remove"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final int[] selectedRows = tblProgrammeSong.getSelectedRows();

            if (selectedRows.length == 0) {
                return;
            }

            if (ISOptionPane.showConfirmDialog(
                    ProgrammeSongInfoPanel.this,
                    selectedRows.length == 1 ? LocaleManager.getString("removeRecordConfirm") : LocaleManager.getString("removeRecordsConfirm"),
                    LocaleManager.getString("remove"), JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                return;
            }
            Arrays.sort(selectedRows);
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                programmeSongTableModel.removeObject(selectedRows[i]);
            }
        }
    }
}
