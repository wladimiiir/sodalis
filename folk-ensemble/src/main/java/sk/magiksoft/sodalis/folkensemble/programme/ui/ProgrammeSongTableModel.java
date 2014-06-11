
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.folkensemble.programme.ui;

import java.util.ArrayList;
import java.util.List;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.sodalis.folkensemble.programme.entity.ProgrammeSong;
import sk.magiksoft.sodalis.person.entity.PersonWrapper;
import sk.magiksoft.swing.ISTable;

/**
 *
 * @author wladimiiir
 */
public class ProgrammeSongTableModel extends ObjectTableModel<ProgrammeSong> {

    public ProgrammeSongTableModel() {
        super(new String[]{
                    LocaleManager.getString("order"),
                    LocaleManager.getString("songName"),
                    LocaleManager.getString("interpreters"),
                    LocaleManager.getString("duration")
                }, new Class[]{
                    ISTable.RIGHT_ALIGNMENT_CLASS,
                    ISTable.LEFT_ALIGNMENT_CLASS,
                    ISTable.LEFT_ALIGNMENT_CLASS,
                    ISTable.RIGHT_ALIGNMENT_CLASS
                });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ProgrammeSong programmeSong = getObject(rowIndex);

        switch (columnIndex) {
            case 0:
                return (rowIndex + 1) + ".";
            case 1:
                return programmeSong.getSong().getName();
            case 2:
                return programmeSong;
            case 3:
                return programmeSong.getSong().getDurationString();
            default:
                return "";
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex != 2) {
            super.setValueAt(aValue, rowIndex, columnIndex);
            return;
        }

        List<PersonWrapper> selected = (List<PersonWrapper>) aValue;
        ProgrammeSong song = getObject(rowIndex);

        song.setInterpreters(new ArrayList<PersonWrapper>(selected));
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 2;
    }
}