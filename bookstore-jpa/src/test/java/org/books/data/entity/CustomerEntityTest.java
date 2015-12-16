package org.books.data.entity;


import org.testng.Assert;
import org.testng.annotations.Test;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class CustomerEntityTest extends AbstractJpaTest {

    @Test
    public void CustomerFindByNameTest() {
        TypedQuery<Customer> query = em.createNamedQuery("Customer.findByLastNameOrFirstName", Customer.class);
        final String firstName = "Alice";
        final String lastName = "Smith";

        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);

        Customer customer = query.getSingleResult();
        Assert.assertEquals(firstName, customer.getFirstName());
        Assert.assertEquals(lastName, customer.getLastName());

        // Check upper / lower case
        query = em.createNamedQuery("Customer.findByLastNameOrFirstName", Customer.class);
        query.setParameter("firstName", "ALICE");
        query.setParameter("lastName", "smith");
        Assert.assertEquals(customer, query.getSingleResult());

        // Check upper / lower case
        query = em.createNamedQuery("Customer.findByLastNameOrFirstName", Customer.class);
        query.setParameter("firstName", "ALICE");
        query.setParameter("lastName", "GUGUS");
        Assert.assertEquals(customer, query.getSingleResult());

        // Check upper / lower case
        query = em.createNamedQuery("Customer.findByLastNameOrFirstName", Customer.class);
        query.setParameter("firstName", "GUGUS");
        query.setParameter("lastName", "SMITH");
        Assert.assertEquals(customer, query.getSingleResult());

    }

    @Test(expectedExceptions = NoResultException.class)
    public void CustomerFindByNameTestNullValue() {
        TypedQuery<Book> query = em.createNamedQuery("Customer.findByLastNameOrFirstName", Book.class);
        query.setParameter("firstName", "a");
        query.setParameter("lastName", "b");

        query.getSingleResult();
    }
}
