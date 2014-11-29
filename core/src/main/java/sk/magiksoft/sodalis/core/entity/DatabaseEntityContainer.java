package sk.magiksoft.sodalis.core.entity;

import java.util.List;

/**
 * @author wladimiiir
 */
public interface DatabaseEntityContainer {
    <T extends DatabaseEntity> boolean acceptDatabaseEntity(Class<T> clazz);

    <T extends DatabaseEntity> T getDatabaseEntity(Class<T> clazz);

    <T extends DatabaseEntity> List<T> getDatabaseEntities(Class<T> clazz);
}
