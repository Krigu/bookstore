package org.books.data.dao;

import org.books.data.BasisJpaTest;
import org.books.data.entity.Book;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.math.BigDecimal;

@Test(groups="BookCRUD",dependsOnGroups = "CustomerCRUD")
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
        em.clear();
        Assert.assertNotNull(book.getId());
    }

    @Test(dependsOnMethods = "createBook", expectedExceptions = PersistenceException.class)
    public void checkUniqueISBN() {
        Book checkBook = new Book();
        checkBook.setIsbn(ISBN);
        //Create
        transaction.begin();
        bean.create(checkBook);
        transaction.commit();
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
    public void removeBook() {
        transaction.begin();
        bean.remove(book);
        transaction.commit();
        em.clear();
        bean.find(book.getId());
    }
}
