
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.core.data.local;

import sk.magiksoft.sodalis.core.data.DataService;
import sk.magiksoft.sodalis.core.data.remote.server.DataManager;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: May 18, 2010
 * Time: 6:25:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataLocalService extends DataService {
    @Override
    DataManager getDataManager();
}