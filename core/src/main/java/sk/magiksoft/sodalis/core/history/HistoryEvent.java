
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

import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.locale.LocaleManager;

import java.util.Calendar;

/**
 * @author wladimiiir
 */
public class HistoryEvent extends AbstractDatabaseEntity implements HistoryAction, Comparable<HistoryEvent> {
    protected Calendar date = Calendar.getInstance();
    protected int action;
    protected String actionName;
    protected String description;

    public HistoryEvent() {
    }

    public HistoryEvent(int action) {
        this(Calendar.getInstance(), action);
    }

    public HistoryEvent(Calendar date, int action) {
        this.date = date;
        this.action = action;

        switch (action) {
            case CREATE:
                this.actionName = LocaleManager.getString("created");
                break;
            case UPDATE:
                this.actionName = LocaleManager.getString("updated");
                break;
            case DELETE:
                this.actionName = LocaleManager.getString("deleted");
                break;
            default:
                this.actionName = LocaleManager.getString("unknownAction");
                break;
        }
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActionName() {
        return actionName;
    }

    @Override
    public int compareTo(HistoryEvent o) {
        return date.compareTo(o.date);
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof HistoryEvent) || entity == this) {
            return;
        }
        HistoryEvent event = (HistoryEvent) entity;

        this.date = (Calendar) event.date.clone();
        this.action = event.action;
        this.actionName = event.actionName;
        this.description = event.description;
    }
}