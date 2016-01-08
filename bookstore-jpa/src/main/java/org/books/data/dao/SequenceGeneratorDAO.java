package org.books.data.dao;

import org.apache.log4j.Logger;
import org.books.data.entity.SequenceGenerator;

import javax.ejb.Stateless;
import javax.persistence.*;

/**
 * <h1>BookDAOBean</h1>
 * <p>
 * class DAO for the entity Book
 * </p>
 **/
@Stateless
public class SequenceGeneratorDAO {

    private static final Logger LOGGER = Logger.getLogger(SequenceGeneratorDAO.class);

    @PersistenceContext(unitName = "bookstore")
    protected EntityManager entityManager;

    public Long getNextValue(String sequenceName) {
        LOGGER.info("Find counter by sequenceName : " + sequenceName);
        TypedQuery<SequenceGenerator> query = entityManager.createNamedQuery(SequenceGenerator.FIND_BY_SEQUENCE_NAME, SequenceGenerator.class);
        query.setParameter("sequenceName", sequenceName);
        try {
            SequenceGenerator sequence = query.getSingleResult();
            updateSequence(sequence);
            return sequence.getCounter();
        } catch (NoResultException | NonUniqueResultException e) {
            createSequence(sequenceName);
        }

        return 1l;
    }

    private void createSequence(String sequenceName) {
        SequenceGenerator sg = new SequenceGenerator();
        sg.setSequenceName(sequenceName);
        sg.setCounter(1L);
        entityManager.persist(sg);
        entityManager.flush();
    }

    private void updateSequence(SequenceGenerator sequence) {
        sequence.setCounter(sequence.getCounter() + 1);
        entityManager.persist(sequence);
        entityManager.flush();
    }


}
