package org.books.data.dao;

import java.util.List;

import org.books.data.dto.CustomerInfo;
import org.books.data.PopulateDBJpaTest;
import org.books.data.entity.Customer;
import org.testng.annotations.BeforeClass;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CustomerQueryTest extends PopulateDBJpaTest {

    private static CustomerDAOBean bean;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        PopulateDBJpaTest.setUpBeforeClass();
        bean = new CustomerDAOBean();
        bean.setEntityManager(em);
    }

    @Test
    public void findByCustomerNumber() {
        Customer customer = bean.findByCustomerNumber("C-1");
        Assert.assertNotNull(customer);
        Assert.assertEquals(customer.getNumber(), "C-1");
    }

    @Test
    public void findByName() {
        List<CustomerInfo> customers = bean.search("Alice");
        Assert.assertEquals(customers.size(), 1);
    }

    @Test
    public void findByName2Results() {
        List<CustomerInfo> customers = bean.search("smith");
        Assert.assertEquals(customers.size(), 2);
    }

    @Test
    public void finbByNameNoResult() {
        List<CustomerInfo> customers = bean.search("dupond");
        Assert.assertEquals(customers.size(), 0);
    }

}
