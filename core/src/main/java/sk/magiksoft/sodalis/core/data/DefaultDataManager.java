
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.data;

import sk.magiksoft.sodalis.core.data.remote.client.ClientDataManager;

/**
 *
 * @author wladimiiir
 */
public class DefaultDataManager extends ClientDataManager{
    private static DefaultDataManager instance;

    private DefaultDataManager() {
    }

    public static DefaultDataManager getInstance() {
        if(instance==null){
            instance = new DefaultDataManager();
        }
        return instance;
    }
}