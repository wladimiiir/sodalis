
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/


package sk.magiksoft.sodalis.core.imex;

import sk.magiksoft.sodalis.core.entity.DatabaseEntity;

import java.util.Collection;
import java.util.LinkedList;

public class SodalisTag {

    private Collection<? extends DatabaseEntity> collection;

    SodalisTag() {
    }

    public SodalisTag(Collection collection) {
        super();
        this.collection = new LinkedList<>((Collection<DatabaseEntity>) collection);
    }

    public Collection<? extends DatabaseEntity> getCollection() {
        return collection;
    }

    public void setCollection(Collection<? extends DatabaseEntity> collection) {
        this.collection = collection;
    }
}