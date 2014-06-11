
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.core.service;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Apr 17, 2010
 * Time: 3:26:34 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LocalService extends Service {
    @Override
    String getServiceName();

    @Override
    void initialize();

    void applicationWillExit();

    void registerServiceListener(ServiceListener listener);

    void applicationOpened();
}