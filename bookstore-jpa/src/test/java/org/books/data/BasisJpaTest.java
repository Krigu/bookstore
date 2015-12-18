package org.books.data;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

/**
 *
 * @author tjd
 */
public abstract class BasisJpaTest {

    protected static EntityManagerFactory emf;
    protected static EntityManager em;
    protected static EntityTransaction transaction;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        emf = Persistence.createEntityManagerFactory("bookstoreTest");
        em = emf.createEntityManager();
        transaction = em.getTransaction();
    }

    /*@AfterClass
    public static void tearDownAfterClass() throws Exception {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }*/

    @AfterMethod
    public void rollback() throws Exception {
        if (transaction.isActive()) {
            transaction.rollback();
        }
    }
}
