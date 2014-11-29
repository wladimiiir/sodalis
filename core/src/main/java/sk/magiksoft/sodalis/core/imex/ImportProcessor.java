package sk.magiksoft.sodalis.core.imex;

import sk.magiksoft.sodalis.core.entity.DatabaseEntity;

/**
 * @author wladimiiir
 * @since 2010/11/21
 */
public interface ImportProcessor<T extends DatabaseEntity> {
    T processImport(T entity);

    T findSimilarEntity(T entity);
}
