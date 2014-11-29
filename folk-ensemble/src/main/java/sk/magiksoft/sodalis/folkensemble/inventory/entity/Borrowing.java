package sk.magiksoft.sodalis.folkensemble.inventory.entity;

import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.sodalis.person.entity.PersonWrapper;

import java.util.Calendar;

/**
 * @author wladimiiir
 */
public class Borrowing extends AbstractDatabaseEntity {

    private Calendar from;
    private Calendar to;
    private Person borrower;
    private PersonWrapper borrowerWrapper;
    private boolean returned = false;

    public Borrowing() {
    }

    public Borrowing(Calendar from, Calendar to, Person borrower) {
        this.from = from;
        this.to = to;
        this.borrower = borrower;
        this.borrowerWrapper = new PersonWrapper(borrower);
    }

    public Borrowing(Calendar from, Calendar to, String borrowerName) {
        this.from = from;
        this.to = to;
        this.borrowerWrapper = new PersonWrapper(borrowerName);
    }

    public PersonWrapper getBorrowerWrapper() {
        return borrowerWrapper;
    }

    public void setBorrowerWrapper(PersonWrapper borrowerWrapper) {
        this.borrowerWrapper = borrowerWrapper;
    }

    public Person getBorrower() {
        return borrower;
    }

    public void setBorrower(Person borrower) {
        this.borrower = borrower;
    }

    public String getBorrowerName() {
        return borrowerWrapper == null ? borrower.getFullName(false) : borrowerWrapper.getPersonName();
    }

    public Calendar getFrom() {
        return from;
    }

    public void setFrom(Calendar from) {
        this.from = from;
    }

    public Calendar getTo() {
        return to;
    }

    public void setTo(Calendar to) {
        this.to = to;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof Borrowing)) {
            return;
        }
        Borrowing borrowing = (Borrowing) entity;
        this.borrower = borrowing.borrower;
        this.from = borrowing.from;
        this.to = borrowing.to;
    }

    @Override
    public String toString() {
        return borrowerWrapper != null ? borrowerWrapper.toString() : borrower.getFullName(false);
    }
}
