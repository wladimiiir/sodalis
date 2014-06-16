
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.ui;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.sodalis.person.entity.Contact;
import sk.magiksoft.swing.itemcomponent.ItemComponent;
import sk.magiksoft.swing.table.ComboBoxTableCellEditor;
import sk.magiksoft.swing.table.TextFieldCellEditor;

import javax.swing.table.TableCellEditor;

/**
 * @author wladimiiir
 */
public class ContactComponent extends ItemComponent {

    private final TableCellEditor TABLE_CELL_EDITOR_TYPE = new ComboBoxTableCellEditor(Contact.ContactType.values());
    private final TableCellEditor TABLE_CELL_EDITOR_CONTACT = new TextFieldCellEditor();

    public ContactComponent() {
        super();
    }

    @Override
    public TableCellEditor getCellEditor(int column) {
        switch (column) {
            case 0:
                return TABLE_CELL_EDITOR_TYPE;
            case 1:
                return TABLE_CELL_EDITOR_CONTACT;
            default:
                return super.getCellEditor(column);
        }
    }

    @Override
    protected Object getNewItem() {
        return new Contact(Contact.ContactType.MOBILE_PHONE, "");
    }

    @Override
    protected ObjectTableModel createTableModel() {
        return new ContactTableModel();
    }

    private class ContactTableModel extends ObjectTableModel<Contact> {

        public ContactTableModel() {
            super(new String[]{
                    LocaleManager.getString("type"),
                    LocaleManager.getString("contact")
            });
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Contact contact = getObject(rowIndex);
            switch (columnIndex) {
                case 0:
                    return contact.getContactType();
                case 1:
                    return contact.getContact();
                default:
                    return "";
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            Contact contact = getObject(rowIndex);
            switch (columnIndex) {
                case 0:
                    contact.setContactType((Contact.ContactType) aValue);
                    break;
                case 1:
                    contact.setContact(aValue.toString());
                    break;
                default:
                    super.setValueAt(aValue, rowIndex, columnIndex);
                    return;
            }
            fireTableDataChanged();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }
    }
}