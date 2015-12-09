package org.books.data.entity;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.logging.Logger;

public class AbstractJpaTest {

    protected static final Logger LOGGER = java.util.logging.Logger.getLogger(AbstractJpaTest.class.getName());
    protected static EntityManagerFactory emf;
    protected static EntityManager em;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        emf = Persistence.createEntityManagerFactory("bookstore");
        em = emf.createEntityManager();

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }
}
