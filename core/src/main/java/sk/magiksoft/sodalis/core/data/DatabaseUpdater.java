
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

/**
 *
 * @author wladimiiir
 */
public class DatabaseUpdater {
    public static void main(String[] args) {
        new DatabaseUpdater().updateDB();
    }

    private void updateDB() {
        SchemaCreator.updateDBSchema();
    }
}