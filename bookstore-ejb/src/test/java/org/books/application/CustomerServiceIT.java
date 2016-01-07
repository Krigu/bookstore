package org.books.application;

import org.junit.Test;
import org.testng.annotations.BeforeClass;

import javax.naming.Context;
import javax.naming.InitialContext;


public class CustomerServiceIT {

    private static final String CUSTOMER_SERVICE_NAME = "java:global/bookstore-app/bookstore-ejb/CatalogService";
    private static CustomerService customerService;


    @BeforeClass
    public void lookupService() throws Exception {
        Context jndiContext = new InitialContext();
        customerService = (CustomerService) jndiContext.lookup(CUSTOMER_SERVICE_NAME);
    }

    @Test
    public void testAuthenticateCustomer() throws Exception {

    }

    @Test
    public void testChangePassword() throws Exception {

    }

    @Test
    public void testFindCustomer() throws Exception {

    }

    @Test
    public void testFindCustomerByEmail() throws Exception {

    }

    @Test
    public void testRegisterCustomer() throws Exception {

    }

    @Test
    public void testSearchCustomers() throws Exception {

    }

    @Test
    public void testUpdateCustomer() throws Exception {

    }
}