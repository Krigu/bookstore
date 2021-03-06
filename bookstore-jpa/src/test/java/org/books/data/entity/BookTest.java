package org.books.data.entity;

import org.books.data.PopulateDBJpaTest;
import org.books.data.dao.BookDAOBean;
import org.books.data.dto.BookInfo;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class BookTest extends PopulateDBJpaTest {

    @Test
    public void BookFindByISBNTest() {
        TypedQuery<Book> query = em.createNamedQuery(Book.FIND_BY_ISBN, Book.class);
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

    @Test
    public void BookFindByISBNDAOTest() {
        BookDAOBean bean = new BookDAOBean();
        bean.setEntityManager(em);
        final String isbn = "143024626X";
        Book book = bean.find(isbn);
        Assert.assertEquals(isbn, book.getIsbn());
        Assert.assertEquals("Beginning Java EE 7 (Expert Voice in Java)", book.getTitle());
    }

    @Test
    public void BookFindByISBNDAOTestNullValue() {
        BookDAOBean bean = new BookDAOBean();
        bean.setEntityManager(em);
        Book book = bean.find("123");
        Assert.assertNull(book);
    }
    
    @Test
    public void searchByKeyWordsJava() {
        BookDAOBean bean = new BookDAOBean();
        bean.setEntityManager(em);

        List<BookInfo> searchList = bean.search("java");
        Assert.assertEquals(searchList.size(), 11);
    }

    @Test
    public void searchByKeyWordsEEJava() {
        BookDAOBean bean = new BookDAOBean();
        bean.setEntityManager(em);

        List<BookInfo> searchList = bean.search("ee java");
        Assert.assertEquals(searchList.size(), 6);
    }
    
    @Test
    public void searchByKeyWordsJavaEEQuoted() {
        BookDAOBean bean = new BookDAOBean();
        bean.setEntityManager(em);

        List<BookInfo> searchList = bean.search("\"java ee\"");
        Assert.assertEquals(searchList.size(), 5);
    }

    @Test
    public void searchByKeyWordsApressBeginning() {
        BookDAOBean bean = new BookDAOBean();
        bean.setEntityManager(em);

        List<BookInfo> searchList = bean.search("Apress beginning");
        Assert.assertEquals(searchList.size(), 2);
    }

    @Test
    public void searchByKeyWords2EJB() {
        BookDAOBean bean = new BookDAOBean();
        bean.setEntityManager(em);

        List<BookInfo> searchList = bean.search("ejb java beginning ejb");
        Assert.assertEquals(searchList.size(), 1);
    }
    
    @Test
    public void searchByKeyWordsMicrosoft() {
        BookDAOBean bean = new BookDAOBean();
        bean.setEntityManager(em);

        List<BookInfo> searchList = bean.search("Microsoft");
        Assert.assertEquals(searchList.size(), 0);
    }


    
}
