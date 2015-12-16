/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.data.entity;

import org.books.data.dao.BookDAOBean;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;

/**
 * @author tjd
 */
public class BookCRUDTest extends AbstractJpaTest {

    private static final String ISBN = "isbn12345";

    @Test
    public void crudBook() {
        EntityTransaction tx = em.getTransaction();
        BookDAOBean bean = new BookDAOBean();
        bean.setEntityManager(em);

        //Create
        Book book = new Book(ISBN, "title", "authors", "publisher", 2015, Book.Binding.Ebook, 300, new BigDecimal(12.34));
        tx.begin();
        Book createBook = bean.create(book);
        tx.commit();

        Assert.assertTrue(createBook.getId() != 0);
        Assert.assertTrue(createBook.getIsbn().equals(ISBN));

        //Find
        Book findBook = bean.find(createBook.getId());
        Assert.assertEquals(createBook.getId(), findBook.getId());

        //Update
        String newTitle = "new title";
        findBook.setTitle(newTitle);
        tx.begin();
        Book updateBook = bean.update(findBook);
        tx.commit();
        Assert.assertTrue(updateBook.getTitle().equals(newTitle));

        //Delete
        tx.begin();
        bean.remove(updateBook);
        tx.commit();

        try {
            bean.find(updateBook.getId());
            Assert.assertTrue(false);
        } catch (EntityNotFoundException e) {
            Assert.assertTrue(true);
        }
    }
}
