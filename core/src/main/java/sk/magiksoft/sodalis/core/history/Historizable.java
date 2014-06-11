
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

import sk.magiksoft.sodalis.core.logger.LogInfo;

import java.util.List;

/**
 * @author wladimiiir
 */
public interface Historizable extends LogInfo {
    void addHistoryEvent(HistoryEvent event);

    List<HistoryEvent> getHistoryEvents(Long entityID);

    String getUpdater();
}