package org.books.data;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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

    @AfterMethod
    public void rollback() throws Exception {
        if (transaction.isActive()) {
            transaction.rollback();
        }
    }
}
