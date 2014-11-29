package sk.magiksoft.sodalis.core.data;

import sk.magiksoft.sodalis.core.entity.DatabaseEntity;

import java.util.List;

/**
 * @author wladimiiir
 */
public interface DataListener {
    public void entitiesAdded(List<? extends DatabaseEntity> entities);

    public void entitiesUpdated(List<? extends DatabaseEntity> entities);

    public void entitiesRemoved(List<? extends DatabaseEntity> entities);
}
