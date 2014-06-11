
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.folkensemble.inventory.entity;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jul 1, 2010
 * Time: 4:19:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class BorrowingWrapper {
    private Borrowing borrowing;
    private InventoryItem inventoryItem;

    public BorrowingWrapper(Borrowing borrowing, InventoryItem inventoryItem) {
        this.borrowing = borrowing;
        this.inventoryItem = inventoryItem;
    }

    public Borrowing getBorrowing() {
        return borrowing;
    }

    public InventoryItem getInventoryItem() {
        return inventoryItem;
    }
}