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
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import org.books.data.entity.PopulateDBJpaTest;
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
        Assert.assertEquals(1,1);
    }

    @Test(dependsOnMethods = "createBook")
    public void crudBook() {

        //em.clear();
        //Find
        Book findBook = bean.find(book.getId());
        Assert.assertEquals(book.getId(), findBook.getId());

        //Update
        String newTitle = "new title";
        findBook.setTitle(newTitle);
        transaction.begin();
        Book updateBook = bean.update(findBook);
        transaction.commit();
        Assert.assertTrue(updateBook.getTitle().equals(newTitle));

        //Delete
        transaction.begin();
        bean.remove(updateBook);
        transaction.commit();

        try {
            bean.find(updateBook.getId());
            Assert.assertTrue(false);
        } catch (EntityNotFoundException e) {
            Assert.assertTrue(true);
        }
    }
}
