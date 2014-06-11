
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.data.remote.server;

import java.util.List;
import sk.magiksoft.sodalis.core.service.ServiceEvent;

/**
 *
 * @author wladimiiir
 */
public class DataServiceEvent extends ServiceEvent{

    public static final int ACTION_RECORDS_ADDED = 1;
    public static final int ACTION_RECORDS_UPDATED = 2;
    public static final int ACTION_RECORDS_REMOVED = 3;

    private List records;

    public DataServiceEvent(int action, List records) {
        super(action);
        this.records = records;
    }

    public List getRecords() {
        return records;
    }
}