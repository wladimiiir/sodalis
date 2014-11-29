package sk.magiksoft.sodalis.person.entity;

import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.entity.ImageEntity;
import sk.magiksoft.sodalis.core.search.FullText;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author wladimiiir
 */
public class PrivatePersonData extends AbstractDatabaseEntity implements PersonData {
    private static final long serialVersionUID = -1L;

    private Calendar birthDate = Calendar.getInstance();
    private ImageEntity photoImageEntity = new ImageEntity();
    @FullText
    private List<Address> addresses = new ArrayList<Address>();
    @FullText
    private List<Contact> contacts = new ArrayList<Contact>();

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Calendar getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Calendar birthDate) {
        this.birthDate = birthDate;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public ImageEntity getPhotoImageEntity() {
        return photoImageEntity;
    }

    public void setPhoto(Image image) {
        photoImageEntity.setImage(image);
    }

    public void setPhotoImageEntity(ImageEntity photoImageEntity) {
        this.photoImageEntity = photoImageEntity;
    }

    public Image getPhoto() {
        return photoImageEntity == null ? null : photoImageEntity.getImage();
    }


    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof PrivatePersonData) || entity == this) {
            return;
        }

        PrivatePersonData data = (PrivatePersonData) entity;

        this.birthDate = (Calendar) data.birthDate.clone();
        this.photoImageEntity.updateFrom(data.photoImageEntity);
        this.addresses.clear();
        this.addresses.addAll(data.addresses);
        this.contacts.clear();
        this.contacts.addAll(data.contacts);
    }
}
