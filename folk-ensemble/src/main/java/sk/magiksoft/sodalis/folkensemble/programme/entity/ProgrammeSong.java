
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.folkensemble.programme.entity;

import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song;
import sk.magiksoft.sodalis.person.entity.PersonWrapper;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class ProgrammeSong extends AbstractDatabaseEntity {

    private Song song;
    private List<PersonWrapper> interpreters = new LinkedList<PersonWrapper>();

    public ProgrammeSong() {
    }

    public ProgrammeSong(Song song) {
        this.song = song;
        for (PersonWrapper personWrapper : song.getInterpreters()) {
            interpreters.add(new PersonWrapper(personWrapper));
        }
    }

    public List<PersonWrapper> getInterpreters() {
        return interpreters;
    }

    public void setInterpreters(List<PersonWrapper> interpreters) {
        this.interpreters = interpreters;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
    }
}