
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

import org.hibernate.cfg.Configuration;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.factory.EntityFactory;
import sk.magiksoft.sodalis.person.entity.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class FieldManager {

    private static FieldManager instance;

    public static synchronized FieldManager getInstance() {
        if (instance == null) {
            instance = new FieldManager();
        }
        return instance;
    }

    public List<String> getEntityFields(DatabaseEntity entity) {
        final Configuration configuration = SodalisApplication.getDBManager().getConfiguration();
        List<String> fields = new ArrayList<String>();


        return fields;
    }

    public static void main(String[] args) {
        FieldManager.getInstance().getEntityFields(EntityFactory.getInstance().createEntity(Person.class));
    }
}