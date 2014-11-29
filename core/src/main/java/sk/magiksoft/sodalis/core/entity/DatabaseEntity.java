package sk.magiksoft.sodalis.core.entity;

import java.io.Serializable;

/**
 * @author wladimiiir
 */
public interface DatabaseEntity extends Serializable, Cloneable, Entity {
    void setId(Long id);

    Long getId();

    void setInternalID(Long internalID);

    Long getInternalID();

    void setUpdater(String updater);

    String getUpdater();

    void updateFrom(DatabaseEntity entity);

    boolean isDeleted();

    void clearIDs();

    Object clone();
}
