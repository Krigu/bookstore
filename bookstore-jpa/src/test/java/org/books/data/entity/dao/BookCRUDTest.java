/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.data.entity.dao;

import org.books.data.dao.BookDAOBean;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import org.books.data.entity.BasisJpaTest;
import org.books.data.entity.Book;
import org.testng.annotations.BeforeClass;

/**
 * @author tjd
 */
public class BookCRUDTest extends BasisJpaTest {

    private static final String ISBN = "isbn12345";
    private BookDAOBean bean;
    private Book book = new Book(ISBN, "title", "authors", "publisher", 2015, Book.Binding.Ebook, 300, new BigDecimal(12.34));

    @BeforeClass
    public void initDAO() throws Exception {
        bean = new BookDAOBean();
        bean.setEntityManager(em);
    }

    @Test
    public void createBook() {
        //Create
        transaction.begin();
        book = bean.create(book);
        transaction.commit();
        Assert.assertNotNull(book.getId());
    }

    @Test(dependsOnMethods = "createBook")
    public void findBookById() {
        em.clear();
        book = bean.find(book.getId());
        em.clear();
        Assert.assertNotNull(book);
    }

    @Test(dependsOnMethods = "findBookById")
    public void updateBook() {
        String newTitle = "new title";
        book.setTitle(newTitle);
        transaction.begin();
        book = bean.update(book);
        transaction.commit();
        em.clear();
        Assert.assertTrue(book.getTitle().equals(newTitle));
    }

    @Test(dependsOnMethods = "updateBook", expectedExceptions = EntityNotFoundException.class)
    public void crudBook() {
        transaction.begin();
        bean.remove(book);
        transaction.commit();
        em.clear();
        bean.find(book.getId());
    }
}
