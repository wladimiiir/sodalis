package sk.magiksoft.swing.list;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Vector;

/**
 * @author wladimiiir
 */
public abstract class ObjectListModel<E> extends DefaultListModel implements SortableListModel {
    protected Vector<E> delegate = new Vector<E>();

    public ObjectListModel() {
        addListDataListener(new ListDataListener() {

            public void intervalAdded(ListDataEvent e) {
                sort();
            }

            public void intervalRemoved(ListDataEvent e) {
                sort();
            }

            public void contentsChanged(ListDataEvent e) {
                sort();
            }
        });
    }

    public void sort() {
        Collections.sort(delegate, getComparator());
    }

    protected abstract Comparator<E> getComparator();

    @Override
    public int getSize() {
        return delegate.size();
    }

    @Override
    public Object getElementAt(int index) {
        return delegate.elementAt(index);
    }

    @Override
    public void copyInto(Object anArray[]) {
        delegate.copyInto(anArray);
    }

    @Override
    public void trimToSize() {
        delegate.trimToSize();
    }

    @Override
    public void ensureCapacity(int minCapacity) {
        delegate.ensureCapacity(minCapacity);
    }

    @Override
    public void setSize(int newSize) {
        int oldSize = delegate.size();
        delegate.setSize(newSize);
        if (oldSize > newSize) {
            fireIntervalRemoved(this, newSize, oldSize - 1);
        } else if (oldSize < newSize) {
            fireIntervalAdded(this, oldSize, newSize - 1);
        }
    }

    @Override
    public int capacity() {
        return delegate.capacity();
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public Enumeration<E> elements() {
        return delegate.elements();
    }

    @Override
    public boolean contains(Object elem) {
        return delegate.contains(elem);
    }

    @Override
    public int indexOf(Object elem) {
        return delegate.indexOf(elem);
    }

    @Override
    public int indexOf(Object elem, int index) {
        return delegate.indexOf(elem, index);
    }

    @Override
    public int lastIndexOf(Object elem) {
        return delegate.lastIndexOf(elem);
    }

    @Override
    public int lastIndexOf(Object elem, int index) {
        return delegate.lastIndexOf(elem, index);
    }

    @Override
    public Object elementAt(int index) {
        return delegate.elementAt(index);
    }

    @Override
    public Object firstElement() {
        return delegate.firstElement();
    }

    @Override
    public Object lastElement() {
        return delegate.lastElement();
    }

    @Override
    public void setElementAt(Object obj, int index) {
        delegate.setElementAt((E) obj, index);
        fireContentsChanged(this, index, index);
    }

    @Override
    public void removeElementAt(int index) {
        delegate.removeElementAt(index);
        fireIntervalRemoved(this, index, index);
    }

    @Override
    public void insertElementAt(Object obj, int index) {
        delegate.insertElementAt((E) obj, index);
        fireIntervalAdded(this, index, index);
    }

    @Override
    public void addElement(Object obj) {
        int index = delegate.size();
        delegate.addElement((E) obj);
        fireIntervalAdded(this, index, index);
    }

    public boolean removeElement(Object obj) {
        int index = indexOf(obj);
        boolean rv = delegate.removeElement(obj);
        if (index >= 0) {
            fireIntervalRemoved(this, index, index);
        }
        return rv;
    }

    @Override
    public void removeAllElements() {
        int index1 = delegate.size() - 1;
        delegate.removeAllElements();
        if (index1 >= 0) {
            fireIntervalRemoved(this, 0, index1);
        }
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public Object[] toArray() {
        Object[] rv = new Object[delegate.size()];
        delegate.copyInto(rv);
        return rv;
    }

    @Override
    public Object get(int index) {
        return delegate.elementAt(index);
    }

    @Override
    public Object set(int index, Object element) {
        Object rv = delegate.elementAt(index);
        delegate.setElementAt((E) element, index);
        fireContentsChanged(this, index, index);
        return rv;
    }

    @Override
    public void add(int index, Object element) {
        delegate.insertElementAt((E) element, index);
        fireIntervalAdded(this, index, index);
    }

    @Override
    public Object remove(int index) {
        Object rv = delegate.elementAt(index);
        delegate.removeElementAt(index);
        fireIntervalRemoved(this, index, index);
        return rv;
    }

    @Override
    public void clear() {
        int index1 = delegate.size() - 1;
        delegate.removeAllElements();
        if (index1 >= 0) {
            fireIntervalRemoved(this, 0, index1);
        }
    }

    @Override
    public void removeRange(int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("fromIndex must be <= toIndex");
        }
        for (int i = toIndex; i >= fromIndex; i--) {
            delegate.removeElementAt(i);
        }
        fireIntervalRemoved(this, fromIndex, toIndex);
    }
}
