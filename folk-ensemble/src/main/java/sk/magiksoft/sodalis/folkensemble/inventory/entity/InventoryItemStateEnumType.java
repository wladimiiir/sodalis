
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.folkensemble.inventory.entity;

import sk.magiksoft.hibernate.IntEnumUserType;

/**
 * @author wladimiiir
 */
public class InventoryItemStateEnumType extends IntEnumUserType<BorrowingInventoryItemData.InventoryItemState> {

    public InventoryItemStateEnumType() {
        super(BorrowingInventoryItemData.InventoryItemState.class, BorrowingInventoryItemData.InventoryItemState.values());
    }
}