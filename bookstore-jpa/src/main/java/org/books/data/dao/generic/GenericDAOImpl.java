package org.books.data.dao.generic;

import org.apache.log4j.Logger;
import org.books.data.entity.BaseEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

/**
 * <h1>GenericDAOImpl</h1>
 * <p>
 * implementation of the generic DAO. All DAOs use to implements this class. It
 * contains the basis CRUD operations.
 * </p>
 * @param <T> Entity
 */
public abstract class GenericDAOImpl<T extends BaseEntity> implements GenericDAO<T> {

    private static final Logger LOGGER = Logger.getLogger(GenericDAO.class);
    private final Class<T> classT;

    @PersistenceContext(unitName = "bookstore")
    protected EntityManager entityManager;

    /**
     * Create a new GenericDAO for the Class<T>
     *
     * @param classT
     */
    public GenericDAOImpl(Class<T> classT) {
        super();
        this.classT = classT;
    }

    @Override
    public T create(T entity) {
        LOGGER.info("creat entity " + classT.getClass().getName());
        entityManager.persist(entity);
        entityManager.flush();
        return entity;
    }

    @Override
    public T find(Long id)  {
        LOGGER.info("find entity " + classT.getClass().getName() + " by id : "
                + id);
        T entity = entityManager.find(classT, id);
        return entity;
    }

    @Override
    public T update(T entity) {
        LOGGER.info("update : " + entity.toString());
        return entityManager.merge(entity);
    }

    @Override
    public void remove(T entity) {
        LOGGER.info("remove : " + entity.toString());
        // Check if the entity is managed (contains()) and if not,
        // then make it managed it (merge()).
        entityManager.remove(entityManager.contains(entity) ? entity
                : entityManager.merge(entity));
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
