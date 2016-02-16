package org.books.integration;

import org.books.application.CatalogService;
import org.books.application.exception.BookAlreadyExistsException;
import org.books.application.exception.BookNotFoundException;
import org.books.application.exception.ValidationException;
import org.books.data.dto.BookDTO;
import org.books.data.dto.BookInfo;
import org.books.data.entity.Book;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.math.BigDecimal;
import java.util.List;
import org.books.application.AmazonCatalog;

@Test(groups ={"CatalogServiceIT"})
public class CatalogServiceIT {

    private static final String CATALOG_SERVICE_NAME = "java:global/bookstore-app/bookstore-ejb/CatalogService";
    private static CatalogService catalogService;

    private static final String AMAZON_CATALOG = "java:global/bookstore-app/bookstore-ejb/AmazonCatalog";
    private static AmazonCatalog amazonCatalog;

    private BookDTO book = new org.books.data.dto.BookDTO("Antonio Goncalves", org.books.data.entity.Book.Binding.Paperback, "143024626X", 608, new BigDecimal("49.99"), 2013, "Apress", "Beginning Java EE 7");

    @BeforeClass
    public void lookupService() throws Exception {
        Context jndiContext = new InitialContext();
        catalogService = (CatalogService) jndiContext.lookup(CATALOG_SERVICE_NAME);
        amazonCatalog = (AmazonCatalog) jndiContext.lookup(AMAZON_CATALOG);
    }

    @Test
    public void findBookAmazon() throws BookNotFoundException {
        BookDTO book = amazonCatalog.findBook("9781430250012");
        Assert.assertEquals(book.getAuthors(), "B V Kumar");
        Assert.assertEquals(book.getPublisher(), "Apress");
        Assert.assertEquals(book.getTitle(), "Oracle Certified Master Java Enterprise Architect Java EE 7: Certification Guide");
        Assert.assertEquals(book.getNumberOfPages().toString(), "700");
        Assert.assertEquals(book.getBinding().toString(), "Paperback");
        Assert.assertEquals(book.getPrice().toString(), "49.99");
        Assert.assertEquals(book.getPublicationYear().toString(), "2015");
    }

    @Test(expectedExceptions = BookNotFoundException.class)
    public void findBookAmazonNotFound() throws BookNotFoundException {
        BookDTO b = amazonCatalog.findBook("1111111111111");
    }

    @Test(expectedExceptions = BookNotFoundException.class)
    public void findBookAmazonNotFoundInvalid() throws BookNotFoundException {
        BookDTO b = amazonCatalog.findBook("112");
    }    
    
    @Test
    public void searchBooksAmazonFound() {
        List<BookInfo> books = amazonCatalog.searchBooks("Java");
        Assert.assertEquals(books.size(), 76);
    }

    @Test
    public void searchBooksAmazonNotFound() {
        List<BookInfo> books = amazonCatalog.searchBooks("akjhfkd");
        Assert.assertEquals(books.size(), 0);
    }
    
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
        List<BookInfo> books = catalogService.searchBooks("Java EE");
        Assert.assertEquals(books.size(), 86);

    }

    @Test
    public void searchBooksEmpty() {
        List<BookInfo> books = catalogService.searchBooks("hjlkdashf");
        Assert.assertEquals(books.size(), 0);

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
