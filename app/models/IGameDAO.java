package models;

import java.util.List;

/**
 * Interface, which the QuizDAO class must implement
 */
public interface IGameDAO {



    /**
     * Persist an entity to the DB. Throw an error in case the given entity already exists
     * @param entity
     * @return
     */
    public void persist(BaseEntity entity);


    /**
     * If no entity with the given id exists in the DB, a new entity is stored. If there is already
     * an entity with the given id, the entity is updated.
     * @param entity
     * @param <T>
     * @return
     */
    public <T extends BaseEntity> T merge(T entity);


    /**
     * Get an entity of the given type using the id
     * @param id
     * @param entityClazz
     * @param <T>
     * @return
     */
    public <T extends BaseEntity> T findEntity(Long id, Class<T> entityClazz);


    /**
     * Get a list of all entities of a certain type
     *
     * @param entityClazz
     * @param <E>
     * @return
     */
    public <E extends BaseEntity> List<E> findEntities(Class<E> entityClazz);

}
