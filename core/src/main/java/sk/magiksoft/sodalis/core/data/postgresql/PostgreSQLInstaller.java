
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.data.postgresql;

import sk.magiksoft.sodalis.core.data.DatabaseInitializer;

/**
 * @author wladimiiir
 */
public class PostgreSQLInstaller {

    public static void main(String[] args) {
        System.setProperty("installation", "TRUE");

        PostgreSQLManager.initDB();
        final PostgreSQLManager manager = new PostgreSQLManager();

        manager.createUser();
        manager.recreateDB();
        DatabaseInitializer.getInstance().initialize();

        System.exit(0);
    }
}