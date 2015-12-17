/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.data.entity.dao;

import org.books.data.dao.CustomerDAOBean;
import org.books.data.dao.OrderDAOBean;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.books.data.entity.Address;
import org.books.data.entity.BasisJpaTest;
import org.books.data.entity.CreditCard;
import org.books.data.entity.Customer;
import org.books.data.entity.Order;
import org.books.data.entity.OrderItem;
import org.testng.annotations.BeforeClass;

/**
 *
 * @author tjd
 */
@Test(groups="OrderCRUD", dependsOnGroups={"BookCRUD","CustomerCRUD"})
public class OrderCRUDTest extends BasisJpaTest {

    private CustomerDAOBean customerBean;
    private OrderDAOBean bean;

    private final Address address = new Address("street", "city", "1234", "CH");
    private final CreditCard cc = new CreditCard(CreditCard.Type.MasterCard, "0123456", 12, 2020);
    private Customer customer = new Customer("customer@2.com", "first name 1", "last name 1", address, cc);
    private final List<OrderItem> items = new ArrayList<>();

    private Order order = new Order("2015", new Date(), BigDecimal.ONE, Order.Status.shipped, customer, customer.getAddress(), customer.getCreditCard(), items);

    @BeforeClass
    public void initDAO() throws Exception {
        customerBean = new CustomerDAOBean();
        customerBean.setEntityManager(em);

        bean = new OrderDAOBean();
        bean.setEntityManager(em);

        transaction.begin();
        customer = customerBean.create(customer);
        transaction.commit();
    }

    @Test
    public void createOrder() {
        //Create

        transaction.begin();
        order = bean.create(order);
        transaction.commit();
        em.clear();
        Assert.assertNotNull(order.getId());
    }

    @Test(dependsOnMethods = "createOrder")
    public void findOrderById() {
        em.clear();
        order = bean.find(order.getId());
        em.clear();
        Assert.assertNotNull(order);
    }

    @Test(dependsOnMethods = "findOrderById")
    public void updateOrder() {
        order.setAmount(BigDecimal.TEN);
        transaction.begin();
        order = bean.update(order);
        transaction.commit();
        Assert.assertEquals(order.getAmount(), BigDecimal.TEN);
    }

    @Test(dependsOnMethods = "updateOrder", expectedExceptions = EntityNotFoundException.class)
    public void removeOrder() {
        transaction.begin();
        bean.remove(order);
        transaction.commit();

        bean.find(order.getId());

    }
}
