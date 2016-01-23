/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.books.application.exception.BookAlreadyExistsException;
import org.books.data.dto.BookDTO;
import org.books.data.entity.Book;

/**
 *
 * @author tjd
 */
@Singleton
//@Startup
public class InitBean {

    private static final Logger LOGGER = Logger.getLogger(InitBean.class.getName());
    //private static final String CATALOG_DATA = "/data/catalog.xml";
    @EJB
    private CatalogService catalogService;

    @PostConstruct
    public void init() {
        /*for (BookDTO book : XmlParser.parse(CATALOG_DATA, BookDTO.class)) {
            addBookDTO(book);
        }*/
        try {
            BookDTO book = new BookDTO("Antonio Goncalves", Book.Binding.Paperback, "143024626X", 608, new BigDecimal("49.99"), 2013, "Apress", "Beginning Java EE 7 (Expert Voice in Java)");
            addBookDTO(book);
            book = new BookDTO("Arun Gupta", Book.Binding.Paperback, "1449370179", 362, new BigDecimal("49.99"), 2013, "O'Reilly Media", "Java EE 7 Essentials");
            addBookDTO(book);
            catalogService.addBook(new BookDTO("Dr. Danny Coward", Book.Binding.Paperback, "0071837345", 512, new BigDecimal("50.00"), 2014, "McGraw-Hill Osborne Media", "Java EE 7: The Big Picture"));
        } catch (BookAlreadyExistsException ex) {
            Logger.getLogger(InitBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addBookDTO(BookDTO book) {
        try {
            catalogService.addBook(book);
        } catch (BookAlreadyExistsException ex) {
            LOGGER.log(Level.WARNING, "Book already exist", ex);
        }
    }
}
