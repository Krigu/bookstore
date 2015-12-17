/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.data.entity;

import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

/**
 *
 * @author tjd
 */
public abstract class BasisJpaTest {

    protected static final Logger LOGGER = Logger.getLogger(BasisJpaTest.class.getName());

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
