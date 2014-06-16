
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

import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import sk.magiksoft.sodalis.core.SodalisApplication;

/**
 * @author wladimiiir
 */
public class SchemaCreator {

    private SchemaCreator() {
    }

    public static void createDBSchema() {
        SchemaExport schemaExport = new SchemaExport(SodalisApplication.getDBManager().getConfiguration());
        schemaExport.execute(true, true, false, false);
    }

    public static void updateDBSchema() {
        SchemaUpdate schemaUpdate = new SchemaUpdate(SodalisApplication.getDBManager().getConfiguration());
        schemaUpdate.execute(true, true);
    }

    public static void main(String[] args) {
        SchemaCreator.createDBSchema();
    }
}