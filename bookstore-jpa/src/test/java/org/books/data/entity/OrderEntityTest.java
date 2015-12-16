package org.books.data.entity;


import org.testng.annotations.Test;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OrderEntityTest extends AbstractJpaTest {

    @Test
    public void OrderfindByNumberTest() {
        TypedQuery<Order> query = em.createNamedQuery("Order.findByNumber", Order.class);
        query.setParameter("number", "12345");

        Order order = query.getSingleResult();

        assertNotNull(order);
        assertEquals("5400000000000005", order.getCreditCard().getNumber());
        assertEquals("Alice", order.getCustomer().getFirstName());

    }

    @Test
    public void OrderfindByCustomerAndYear() {

        TypedQuery<Order> query = em.createNamedQuery("Order.findByCustomerAndYear", Order.class);
        query.setParameter("customerId", 100001);
        query.setParameter("year", "2015");

        Order order = query.getSingleResult();

        assertNotNull(order);
        assertEquals("5400000000000005", order.getCreditCard().getNumber());
        assertEquals("Alice", order.getCustomer().getFirstName());

    }

    @Test(expectedExceptions = NoResultException.class)
    public void OrderfindByCustomerAndYearNoResult() {

        TypedQuery<Order> query = em.createNamedQuery("Order.findByCustomerAndYear", Order.class);
        query.setParameter("customerId", 100001);
        query.setParameter("year", "2012");

        query.getSingleResult();


    }


}
