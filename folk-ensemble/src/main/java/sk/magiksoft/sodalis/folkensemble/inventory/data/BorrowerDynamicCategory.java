package sk.magiksoft.sodalis.folkensemble.inventory.data;

import sk.magiksoft.sodalis.category.CategoryManager;
import sk.magiksoft.sodalis.category.entity.Categorized;
import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.category.entity.DynamicCategory;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.folkensemble.inventory.InventoryModule;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.Borrowing;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.BorrowingInventoryItemData;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.InventoryItem;
import sk.magiksoft.sodalis.person.entity.Person;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class BorrowerDynamicCategory extends DynamicCategory {

    private SoftReference<List<DynamicCategory>> children;

    public BorrowerDynamicCategory() {
        setParentCategory(CategoryManager.getInstance().getRootCategory(InventoryModule.class, false));
        setName(LocaleManager.getString("borrower"));
        setId(-1l);
    }

    private List<DynamicCategory> getBorrowerCategories() {
        List<Person> borrowers = InventoryDataManager.getInstance().getDatabaseEntities("select b.borrower from Borrowing b where b.returned=false");
        List<Person> addedBorrower = new ArrayList<Person>();
        List<DynamicCategory> dynamicCategories = new ArrayList<DynamicCategory>();
        DynamicCategory category;
        long index = -1001;

        for (final Person borrower : borrowers) {
            if (addedBorrower.contains(borrower)) {
                continue;
            }

            category = new DynamicCategory(this, borrower.getFullName(true)) {

                @Override
                public boolean acceptCategorized(Categorized categorized) {
                    List<Borrowing> borrowings = ((InventoryItem) categorized).getInventoryItemData(BorrowingInventoryItemData.class).getBorrowings();

                    for (Borrowing borrowing : borrowings) {
                        if (!borrowing.isReturned() && borrowing.getBorrower().equals(borrower)) {
                            return true;
                        }
                    }

                    return false;
                }

                @Override
                public void refresh() {
                }
            };
            category.setId(index--);

            dynamicCategories.add(category);
        }

        return dynamicCategories;
    }

    @Override
    public List<Category> getChildCategories() {
        if (children == null || children.get() == null) {
            children = new SoftReference<List<DynamicCategory>>(getBorrowerCategories());
        }

        return new ArrayList<Category>(children.get());
    }

    @Override
    public boolean acceptCategorized(Categorized categorized) {
        for (Category category : getChildCategories()) {
            if (((DynamicCategory) category).acceptCategorized(categorized)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void refresh() {
        children = null;
    }

}
