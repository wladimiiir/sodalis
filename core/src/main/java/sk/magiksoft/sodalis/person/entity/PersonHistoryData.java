
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.person.entity;

import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.history.HistoryEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author wladimiiir
 */
public class PersonHistoryData extends AbstractDatabaseEntity implements PersonData {
    private List<HistoryEvent> historyEvents = new ArrayList<HistoryEvent>();

    public PersonHistoryData() {
    }

    public List<HistoryEvent> getHistoryEvents() {
        return historyEvents;
    }

    public List<HistoryEvent> getHistoryEvents(Long entityID) {
        return historyEvents;
    }

    public void setHistoryEvents(List<HistoryEvent> historyEvents) {
        this.historyEvents = historyEvents;
    }

    public void addHistoryEvent(HistoryEvent historyEvent) {
        this.historyEvents.add(historyEvent);
    }

    public HistoryEvent getCurrentHistoryEvent() {
        if (historyEvents.isEmpty()) {
            return null;
        }
        Collections.sort(historyEvents);
        return historyEvents.get(historyEvents.size() - 1);
    }

    public <T extends HistoryEvent> T getCurrentHistoryEvent(Class<T> clazz) {
        if (historyEvents.isEmpty()) {
            return null;
        }
        Collections.sort(historyEvents);
        for (int i = historyEvents.size() - 1; i >= 0; i--) {
            HistoryEvent historyEvent = historyEvents.get(i);
            if (historyEvent.getClass() == clazz) {
                return (T) historyEvent;
            }
        }

        return null;
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof PersonHistoryData)
                || this == entity) {
            return;
        }
        PersonHistoryData historyData = (PersonHistoryData) entity;

        this.historyEvents.clear();
        this.historyEvents.addAll(historyData.historyEvents);
    }
}