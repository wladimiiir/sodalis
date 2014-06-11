
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.folkensemble.inventory.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.history.HistoryEvent;

/**
 *
 * @author wladimiiir
 */
public class InventoryHistoryData extends AbstractDatabaseEntity implements InventoryItemData{
    private List<HistoryEvent> historyEvents = new ArrayList<HistoryEvent>();

    public InventoryHistoryData() {
    }

    public List<HistoryEvent> getHistoryEvents() {
        return historyEvents;
    }

    public void setHistoryEvents(List<HistoryEvent> historyEvents) {
        this.historyEvents = historyEvents;
    }

    public void addHistoryEvent(HistoryEvent historyEvent){
        this.historyEvents.add(historyEvent);
    }

    public HistoryEvent getCurrentHistoryEvent(){
        if(historyEvents.isEmpty()){
            return null;
        }
        Collections.sort(historyEvents);
        return historyEvents.get(historyEvents.size()-1);
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if(!(entity instanceof InventoryHistoryData)
                || this==entity){
            return;
        }
        InventoryHistoryData historyData = (InventoryHistoryData) entity;

        this.historyEvents.clear();
        this.historyEvents.addAll(historyData.historyEvents);
    }
}