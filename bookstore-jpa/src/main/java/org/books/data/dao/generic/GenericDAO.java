package org.books.data.dao.generic;

import javax.persistence.EntityNotFoundException;
import org.books.data.entity.BaseEntity;

/**
 * <h1>GenericDAO</h1>
 * <p>
 * The goal of this interface is to provide the basic CRUD action
 * </p>
 * @param <T> entity
 */
public interface GenericDAO<T extends BaseEntity> {

    /**
     *
     * <h1>T create(T entity)</h1>
     *
     * persist this entity into the current persistence context.
     *
     * @param entity entity instance
     *
     * @return the managed instance
     *
     */
    public T create(T entity);

    /**
     * <h1>T find(Long id)</h1>
     *
     * <p>
     * Find by primary key. Search for an entity of the specified class and
     * primary key.
     * </p>
     * <p>
     * If the entity instance is contained in the persistence context, it is
     * returned from there.
     * </p>
     *
     * @param id Primary key
     * @exception EntityNotFoundException happen if the entity isn't find
     *
     * @return the found entity instance
     */
    public T find(Long id) throws EntityNotFoundException;

    /**
     *
     * <h1>T update(T entity)</h1>
     *
     * Merge the state of the given entity into the current persistence context.
     *
     * @param entity entity instance
     *
     * @return the managed instance that the state was merged to
     *
     */
    public T update(T entity);

    /**
     * <h1>void remove(T entity)</h1>
     *
     * <p>
     * Remove the entity instance.
     * </p>
     *
     * @param entity entity instance
     *
     *
     */
    public void remove(T entity);
}
