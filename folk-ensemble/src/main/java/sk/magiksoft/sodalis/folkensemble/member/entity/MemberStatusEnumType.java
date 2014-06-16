
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.folkensemble.member.entity;

import sk.magiksoft.hibernate.IntEnumUserType;
import sk.magiksoft.sodalis.folkensemble.member.entity.MemberData.MemberStatus;

/**
 * @author wladimiiir
 */
public class MemberStatusEnumType extends IntEnumUserType<MemberStatus> {

    public MemberStatusEnumType() {
        super(MemberStatus.class, MemberStatus.values());
    }
}