package org.books.data.entity.dao;

import java.math.BigDecimal;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import org.books.data.dao.BookDAOBean;
import org.books.data.dao.OrderItemDAOBean;
import org.books.data.BasisJpaTest;
import org.books.data.entity.Book;
import org.books.data.entity.OrderItem;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author tjd
 */
@Test(groups = "OrderItemCRUD", dependsOnGroups = {"BookCRUD", "CustomerCRUD"})
public class OrderItemCRUDTest extends BasisJpaTest {

    private OrderItemDAOBean bean;
    private BookDAOBean bookDAOBean;
    private Book book = new Book("1234", "title", "authors", "publisher", 2015, Book.Binding.Ebook, 300, new BigDecimal(12.34));
    private OrderItem item = new OrderItem();

    @BeforeClass
    public void initDAO() throws Exception {
        bean = new OrderItemDAOBean();
        bean.setEntityManager(em);

        bookDAOBean = new BookDAOBean();
        bookDAOBean.setEntityManager(em);

        transaction.begin();
        book = bookDAOBean.create(book);
        transaction.commit();
        em.clear();

        item.setBook(book);
        item.setPrice(BigDecimal.ONE);
        item.setQuantity(10);
    }

    @Test
    public void createOrderItem() {
        //Create
        transaction.begin();
        item = bean.create(item);
        transaction.commit();
        em.clear();
        Assert.assertNotNull(book.getId());
    }
    
    @Test(dependsOnMethods = "createOrderItem")
    public void findOrderItemById() {
        em.clear();
        item = bean.find(item.getId());
        em.clear();
        Assert.assertNotNull(book);
    }

    @Test(dependsOnMethods = "findOrderItemById")
    public void updateOrderItem() {
        int quantity = 20;
        item.setQuantity(quantity);
        transaction.begin();
        item = bean.update(item);
        transaction.commit();
        em.clear();
        Assert.assertTrue(item.getQuantity()==quantity);
    }

    @Test(dependsOnMethods = "updateOrderItem", expectedExceptions = EntityNotFoundException.class)
    public void removeBook() {
        transaction.begin();
        bean.remove(item);
        transaction.commit();
        em.clear();
        bean.find(item.getId());
    }
}
