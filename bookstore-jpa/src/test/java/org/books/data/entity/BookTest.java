package org.books.data.entity;


import org.junit.Assert;
import org.junit.Test;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class BookTest extends AbstractJpaTest {

    @Test
    public void BookFindByISBNTest() {
        TypedQuery<Book> query = em.createNamedQuery("Book.findByISBN", Book.class);
        final String isbn = "143024626X";
        query.setParameter("isbn", isbn);

        Book book = query.getSingleResult();
        Assert.assertEquals(isbn, book.getIsbn());
        Assert.assertEquals("Beginning Java EE 7 (Expert Voice in Java)", book.getTitle());

    }

    @Test(expected = NoResultException.class)
    public void BookFindByISBNTestNullValue() {
        TypedQuery<Book> query = em.createNamedQuery("Book.findByISBN", Book.class);
        query.setParameter("isbn", null);

        Book book = query.getSingleResult();
    }
}
