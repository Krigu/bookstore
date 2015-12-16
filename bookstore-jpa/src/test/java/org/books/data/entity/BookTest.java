package org.books.data.entity;


import org.testng.Assert;
import org.testng.annotations.Test;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class BookTest extends PopulateDBJpaTest {

    @Test
    public void BookFindByISBNTest() {
        TypedQuery<Book> query = em.createNamedQuery("Book.findByISBN", Book.class);
        final String isbn = "143024626X";
        query.setParameter("isbn", isbn);

        Book book = query.getSingleResult();
        Assert.assertEquals(isbn, book.getIsbn());
        Assert.assertEquals("Beginning Java EE 7 (Expert Voice in Java)", book.getTitle());

    }

    @Test(expectedExceptions = NoResultException.class)
    public void BookFindByISBNTestNullValue() {
        TypedQuery<Book> query = em.createNamedQuery("Book.findByISBN", Book.class);
        query.setParameter("isbn", "123");

        Book book = query.getSingleResult();
    }
}
