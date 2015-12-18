package org.books.data.entity.dao;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.books.data.dao.CustomerDAOBean;
import org.books.data.dao.OrderDAOBean;
import org.books.data.dto.OrderInfo;
import org.books.data.entity.Customer;
import org.books.data.entity.Order;
import org.books.data.PopulateDBJpaTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class OrderQueryTest extends PopulateDBJpaTest {

    private static OrderDAOBean bean;
    private static CustomerDAOBean customerBean;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        PopulateDBJpaTest.setUpBeforeClass();
        bean = new OrderDAOBean();
        bean.setEntityManager(em);
        customerBean = new CustomerDAOBean();
        customerBean.setEntityManager(em);
    }

    @Test
    public void findByNumberTest() {
        Order order = bean.find("2014-1");
        Assert.assertNotNull(order);
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void findByNumberNoResultsTest() {
        bean.find("2010-1");
    }

    @Test
    public void searchByCustomerTest() {
        Customer customer = customerBean.find((long)10001);
        List<OrderInfo> orders = bean.search(customer,2014);
        Assert.assertEquals(orders.size(), 2);
    }

    @Test
    public void searchByCustomerNoResultsTest() {
        Customer customer = customerBean.find((long)10001);
        List<OrderInfo> orders = bean.search(customer,2000);
        Assert.assertEquals(orders.size(), 0);
    }
}
