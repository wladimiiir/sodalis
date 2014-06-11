
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.updater;

import sk.magiksoft.sodalis.core.data.DefaultDataManager;
import sk.magiksoft.sodalis.core.update.updater.Updater;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 2/6/11
 * Time: 5:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class HistoryEventDescriptionLengthUpdater implements Updater {
    @Override public void runUpdate() throws Exception {
        DefaultDataManager.getInstance().executeSQLQuery("alter table historyevent alter column description varchar(65536)");
    }
}