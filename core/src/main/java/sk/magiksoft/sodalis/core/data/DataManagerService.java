
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

import sk.magiksoft.sodalis.core.logger.LoggerManager;

/**
 * @author wladimiiir
 */
public class DataManagerService {
    private static DataManagerService instance;

    private DataManagerService() {
    }

    public static DataManagerService getInstance() {
        if (instance == null) {
            instance = new DataManagerService();
        }
        return instance;
    }

    public static void main(String[] args) {
        try {
            DefaultDataManager.class.newInstance();
        } catch (InstantiationException ex) {
            LoggerManager.getInstance().error(DataManagerService.class, ex);
        } catch (IllegalAccessException ex) {
            LoggerManager.getInstance().error(DataManagerService.class, ex);
        }
    }
}