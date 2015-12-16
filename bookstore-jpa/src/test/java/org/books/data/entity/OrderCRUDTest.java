/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.data.entity;

import org.books.data.dao.CustomerDAOBean;
import org.books.data.dao.OrderDAOBean;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author tjd
 */
public class OrderCRUDTest extends AbstractJpaTest {
    
    @Test
    public void crudOrder() {
        EntityTransaction tx = em.getTransaction();
        
        CustomerDAOBean customerBean = new CustomerDAOBean();
        customerBean.setEntityManager(em);
        Address address = new Address("street", "city", "1234", "CH");
        CreditCard cc = new CreditCard(CreditCard.Type.MasterCard, "0123456", 12, 2020);
        Customer customer = new Customer("b@2.com", "first name 1", "last name 1", address, cc);
        tx.begin();
        customer = customerBean.create(customer);
        tx.commit();

        OrderDAOBean bean = new OrderDAOBean();
        bean.setEntityManager(em);

        //Create
        List<OrderItem> items = new ArrayList<>();
        Order order = new Order("2015", new Date(), BigDecimal.ONE, Order.Status.shipped, customer, customer.getAddress(), customer.getCreditCard(), items);
        tx.begin();
        Order createOrder = bean.create(order);
        tx.commit();
        
        Assert.assertTrue(createOrder.getId() != 0);

        //Find
        Order findOrder = bean.find(createOrder.getId());
        Assert.assertEquals(createOrder.getId(), findOrder.getId());

        //Update
        findOrder.setAmount(BigDecimal.TEN);
        tx.begin();
        Order updateOrder = bean.update(findOrder);
        tx.commit();
        Assert.assertEquals(updateOrder.getAmount(), BigDecimal.TEN);

        //Delete
        tx.begin();
        bean.remove(updateOrder);
        tx.commit();
        
        try {
            bean.find(updateOrder.getId());
            Assert.assertTrue(false);
        } catch (EntityNotFoundException e) {
            Assert.assertTrue(true);
        }
    }
}
