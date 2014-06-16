
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.history;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.swing.ISTable;

import java.text.Collator;
import java.util.Comparator;

/**
 * @author wladimiiir
 */
public class HistoryEventTableModel extends ObjectTableModel<HistoryEvent> {

    private static final Comparator<HistoryEvent> ACTION_COMPARATOR = new Comparator<HistoryEvent>() {

        @Override
        public int compare(HistoryEvent o1, HistoryEvent o2) {
            return o2.getAction() - o1.getAction();
        }
    };
    private static final Comparator<HistoryEvent> DATE_COMPARATOR = new Comparator<HistoryEvent>() {

        @Override
        public int compare(HistoryEvent o1, HistoryEvent o2) {
            return o1.getDate().compareTo(o2.getDate());
        }
    };
    private static final Comparator<HistoryEvent> UPDATER_COMPARATOR = new Comparator<HistoryEvent>() {

        @Override
        public int compare(HistoryEvent o1, HistoryEvent o2) {
            return Collator.getInstance().compare(o1.getReadableUpdater(), o2.getReadableUpdater());
        }
    };
    private static final Comparator<HistoryEvent> DESCRIPTION_COMPARATOR = new Comparator<HistoryEvent>() {

        @Override
        public int compare(HistoryEvent o1, HistoryEvent o2) {
            String d1 = o1.getDescription() == null ? "" : o1.getDescription();
            String d2 = o2.getDescription() == null ? "" : o2.getDescription();

            return Collator.getInstance().compare(d1, d2);
        }
    };

    public HistoryEventTableModel() {
        super(
                new String[]{
                        LocaleManager.getString("action"),
                        LocaleManager.getString("updater"),
                        LocaleManager.getString("date"),
                        LocaleManager.getString("description")
                },
                new Class[]{
                        ISTable.LEFT_ALIGNMENT_CLASS,
                        ISTable.LEFT_ALIGNMENT_CLASS,
                        ISTable.RIGHT_ALIGNMENT_CLASS,
                        ISTable.LEFT_ALIGNMENT_CLASS
                });
    }

    @Override
    public Comparator getComparator(int column) {
        switch (column) {
            case 0:
                return ACTION_COMPARATOR;
            case 1:
                return UPDATER_COMPARATOR;
            case 2:
                return DATE_COMPARATOR;
            case 3:
                return DESCRIPTION_COMPARATOR;
            default:
                return super.getComparator(column);
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        HistoryEvent historyEvent = getObject(rowIndex);

        switch (columnIndex) {
            case 0:
                return historyEvent.getActionName();
            case 1:
                return historyEvent.getReadableUpdater();
            case 2:
                return DATE_TIME_FORMAT.format(historyEvent.getDate().getTime());
            case 3:
                return historyEvent.getDescription() == null ? "" : historyEvent.getDescription();
            default:
                return "";
        }
    }
}