package org.books.integration;

import org.books.application.CatalogService;
import org.books.application.CatalogServiceBean;
import org.books.application.exception.BookAlreadyExistsException;
import org.books.application.exception.BookNotFoundException;
import org.books.application.exception.ValidationException;
import org.books.data.dao.BookDAOBean;
import org.books.data.dao.BookDAOLocal;
import org.books.data.dao.generic.GenericDAO;
import org.books.data.dto.BookDTO;
import org.books.data.dto.BookInfo;
import org.books.data.entity.BaseEntity;
import org.books.data.entity.Book;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ejb.EJB;
import java.math.BigDecimal;
import java.util.List;

@Test(groups = {"CatalogServiceTest"})
public class CatalogServiceTest extends BookstoreArquillianTest {


    private BookDTO book = new org.books.data.dto.BookDTO("Antonio Goncalves", org.books.data.entity.Book.Binding.Paperback, "143024626X", 608, new BigDecimal("49.99"), 2013, "Apress", "Beginning Java EE 7");


//    @Deployment
//    public static Archive<?> createDeployment() {
//
//        return ShrinkWrap.create(JavaArchive.class)
//                .addClasses(CatalogService.class,
//                        CatalogServiceBean.class,
//                        BookDAOLocal.class,
//                        BookDAOBean.class,
//                        GenericDAO.class,
//                        Book.class,
//                        BaseEntity.class,
//                        ValidationException.class)
//                .addAsManifestResource("test-persistence.xml", "persistence.xml")
//                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
//    }

    @EJB
    private CatalogService catalogService;

    @Test(expectedExceptions = ValidationException.class)
    public void validationTestInvalidISBN() throws BookAlreadyExistsException {
        BookDTO b = new BookDTO();
        b.setAuthors("authors");
        // 9 instad of minimal 10 digits
        b.setIsbn("231232341");
        b.setNumberOfPages(123);
        b.setPublicationYear(2000);
        b.setPrice(new BigDecimal(12));
        b.setPublisher("publisher");
        b.setBinding(Book.Binding.Ebook);
        b.setTitle("Title");

        catalogService.addBook(b);
    }

    @Test(expectedExceptions = ValidationException.class)
    public void validationTestTitleIsNull() throws BookAlreadyExistsException {
        BookDTO b = new BookDTO();
        b.setAuthors("authors");
        b.setIsbn("2312323412");
        b.setNumberOfPages(123);
        b.setPublicationYear(2000);
        b.setPrice(new BigDecimal(12));
        b.setPublisher("publisher");
        b.setBinding(Book.Binding.Ebook);

        catalogService.addBook(b);
    }

    @Test
    public void addBook() throws BookAlreadyExistsException {
        catalogService.addBook(book);
    }

    @Test(dependsOnMethods = "addBook", expectedExceptions = BookAlreadyExistsException.class)
    public void addExistingBook() throws BookAlreadyExistsException {
        catalogService.addBook(book);
    }

    @Test(dependsOnMethods = "addBook")
    public void findBook() throws BookNotFoundException {
        Assert.assertNotNull(catalogService.findBook(book.getIsbn()));
    }

    @Test(expectedExceptions = BookNotFoundException.class)
    public void findBookNotExisting() throws BookNotFoundException {
        catalogService.findBook("123");
    }

    @Test(dependsOnMethods = "addBook")
    public void searchBooks() {
        List<BookInfo> books = catalogService.searchBooks("Java");
        Assert.assertEquals(1, books.size());

    }

    @Test
    public void searchBooksEmpty() {
        List<BookInfo> books = catalogService.searchBooks("hjlkdashf");
        Assert.assertEquals(0, books.size());

    }

    @Test(dependsOnMethods = "addBook")
    public void updateBook() throws BookNotFoundException {
        book.setPrice(new BigDecimal("59.99"));
        catalogService.updateBook(book);
        Assert.assertEquals(new BigDecimal("59.99"), catalogService.findBook(book.getIsbn()).getPrice());
    }

    @Test(expectedExceptions = BookNotFoundException.class)
    public void updateBookNotFound() throws BookNotFoundException {
        BookDTO book = new BookDTO();
        book.setAuthors("authors");
        book.setIsbn("2312323423412");
        book.setNumberOfPages(123);
        book.setPublicationYear(2000);
        book.setPrice(new BigDecimal(12));
        book.setPublisher("publisher");
        book.setBinding(Book.Binding.Ebook);
        book.setTitle("Test");
        catalogService.updateBook(book);
    }
}
