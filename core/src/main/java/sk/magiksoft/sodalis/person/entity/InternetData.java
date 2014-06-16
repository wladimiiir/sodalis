
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.person.entity;

import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.entity.EmailServer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class InternetData extends AbstractDatabaseEntity implements PersonData {

    private List<EmailServer> emailServers = new ArrayList<EmailServer>();

    public List<EmailServer> getEmailServers() {
        return emailServers;
    }

    public void setEmailServers(List<EmailServer> emailServers) {
        this.emailServers = emailServers;
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof InternetData) || entity == this) {
            return;
        }

        InternetData data = (InternetData) entity;
        this.emailServers.clear();
        this.emailServers.addAll(data.emailServers);
    }
}