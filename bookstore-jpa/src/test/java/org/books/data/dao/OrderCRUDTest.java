package org.books.data.dao;

import org.books.data.BasisJpaTest;
import org.books.data.dto.CreditCardType;
import org.books.data.entity.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Test(groups = "OrderCRUD", dependsOnGroups = {"BookCRUD", "CustomerCRUD"})
public class OrderCRUDTest extends BasisJpaTest {

    private CustomerDAOBean customerBean;
    private OrderDAOBean bean;
    private BookDAOBean bookDAOBean;
    private OrderItemDAOBean orderItemDAOBean;

    private final Address address = new Address("street", "city", "1234", "CH");
    private final CreditCard cc = new CreditCard(CreditCardType.MasterCard.MasterCard, "0123456", 12, 2020);
    private Customer customer = new Customer("customer@2.com", "first name 1", "last name 1", "1C", address, cc);
    private Book book = new Book();
    private final List<OrderItem> items = new ArrayList<>();

    private Order order = new Order("2015", new Date(), BigDecimal.ONE, Order.Status.shipped, customer, customer.getAddress(), customer.getCreditCard(), items);
    private OrderItem orderItem = new OrderItem();

    @BeforeClass
    public void initDAO() throws Exception {
        customerBean = new CustomerDAOBean();
        customerBean.setEntityManager(em);

        bean = new OrderDAOBean();
        bean.setEntityManager(em);

        bookDAOBean = new BookDAOBean();
        bookDAOBean.setEntityManager(em);

        orderItemDAOBean = new OrderItemDAOBean();
        orderItemDAOBean.setEntityManager(em);

        book.setIsbn("12345678");

        transaction.begin();
        customer = customerBean.create(customer);
        book = bookDAOBean.create(book);
        transaction.commit();
    }

    @Test
    public void createOrder() {
        transaction.begin();
        order = bean.create(order);
        transaction.commit();
        em.clear();
        Assert.assertNotNull(order.getId());
    }

    @Test(dependsOnMethods = "createOrder", expectedExceptions = PersistenceException.class)
    public void checkMandatoryCustomer() {
        Order orderWithoutCustomer = new Order("2016", new Date(), BigDecimal.ONE, Order.Status.shipped, null, customer.getAddress(), customer.getCreditCard(), items);
        transaction.begin();
        bean.create(orderWithoutCustomer);
        transaction.commit();
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

    @Test(dependsOnMethods = "updateOrder")
    public void addOrderItem() {
        orderItem.setBook(book);
        orderItem.setQuantity(1);
        orderItem.setPrice(new BigDecimal(30));
        order.getItems().add(orderItem);
        transaction.begin();
        order = bean.update(order);
        transaction.commit();
        Assert.assertEquals(order.getItems().size(), 1);
        orderItem = order.getItems().get(0);
    }

    @Test(dependsOnMethods = "updateOrder")
    public void removeOrder() {
        transaction.begin();
        bean.remove(order);
        transaction.commit();

        Assert.assertNull(bean.find(order.getId()));
    }

    @Test(dependsOnMethods = "removeOrder")
    public void orphanRemovalOrderItem() {
        Assert.assertNull(orderItemDAOBean.find(orderItem.getId()));;
    }
}
