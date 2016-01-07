package org.books.application;

import org.books.application.exception.BookAlreadyExistsException;
import org.books.application.exception.BookNotFoundException;
import org.books.data.entity.Book;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import javax.naming.Context;
import javax.naming.InitialContext;
import java.math.BigDecimal;
import java.util.List;
import junit.framework.Assert;
import org.books.data.dto.BookDTO;
import org.books.data.dto.BookInfo;

import static org.testng.Assert.assertNotNull;


public class CatalogServiceIT {

    private static final String CATALOG_SERVICE_NAME = "java:global/bookstore-app/bookstore-ejb/CatalogService";
    private static CatalogService catalogService;

    private BookDTO book = new org.books.data.dto.BookDTO("Antonio Goncalves", org.books.data.entity.Book.Binding.Paperback, "143024626X", 608, new BigDecimal("49.99"), 2013, "Apress", "Beginning Java EE 7");

    @BeforeClass
    public void lookupService() throws Exception {
        Context jndiContext = new InitialContext();
        catalogService = (CatalogService) jndiContext.lookup(CATALOG_SERVICE_NAME);
    }

    @Test
    public void addBook() throws BookAlreadyExistsException {
        catalogService.addBook(book);
    }

    @Test(dependsOnMethods = "addBook")
    public void findBook() throws BookNotFoundException {
        assertNotNull(catalogService.findBook(book.getIsbn()));
    }
    
    @Test(dependsOnMethods = "addBook")
    public void searchBooks() {
        List<BookInfo> books = catalogService.searchBooks("Java");
        Assert.assertEquals(1, books.size());
        
    }
    
    @Test(dependsOnMethods = "addBook")
    public void updateBook() throws BookNotFoundException {
        book.setPrice(new BigDecimal("59.99"));
        catalogService.updateBook(book);
        Assert.assertEquals(new BigDecimal("59.99"), catalogService.findBook(book.getIsbn()).getPrice());
    }
    

    
}
