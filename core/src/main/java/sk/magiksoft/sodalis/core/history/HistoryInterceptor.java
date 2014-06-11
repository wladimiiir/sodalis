
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

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import sk.magiksoft.sodalis.core.data.remote.server.impl.DataManagerImpl;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.logger.*;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Iterator;

/**
 *
 * @author wladimiiir
 */
public class HistoryInterceptor extends EmptyInterceptor {

    private DataManagerImpl dataManagerImpl;

    public DataManagerImpl getDataManagerImpl() {
        if (dataManagerImpl == null) {
            try {
                dataManagerImpl = new DataManagerImpl();
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }
        return dataManagerImpl;
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (entity instanceof Historizable) {
            final HistoryEvent historyEvent = new HistoryEvent(HistoryAction.CREATE);
            historyEvent.setUpdater(((Historizable)entity).getUpdater());
            historyEvent.setDescription(LogInfoManager.createLogInfo((Historizable) entity));
            ((Historizable) entity).addHistoryEvent(historyEvent);
        }

        return super.onSave(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        int index;
        HistoryEvent historyEvent;

        if (entity instanceof Historizable) {
            for (index = 0; index < propertyNames.length; index++) {
                String propertyName = propertyNames[index];

                if (propertyName.equals("deleted")) {
                    break;
                }
            }
            if (index == propertyNames.length || previousState == null || currentState[index].equals(previousState[index])) {
                index = -1;
            }
            if (entity instanceof DatabaseEntity && index != -1) {
                historyEvent = new HistoryEvent(currentState[index].equals(Boolean.TRUE)
                        ? HistoryAction.DELETE
                        : HistoryAction.UNDELETE);
            } else {
                historyEvent = new HistoryEvent(HistoryAction.UPDATE);
            }
            historyEvent.setUpdater(((Historizable)entity).getUpdater());
            historyEvent.setDescription(LogInfoManager.createLogInfo((Historizable) entity));
            historyEvent = (HistoryEvent) getDataManagerImpl().addDatabaseEntity(null, historyEvent);
            ((Historizable) entity).addHistoryEvent(historyEvent);
        }

        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }
}