package org.books.util;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.books.application.CatalogService;
import org.books.application.exception.BookAlreadyExistsException;
import org.books.data.dto.BookDTO;
import org.books.data.entity.Book;
import org.books.presentation.bean.catalog.CatalogBean;

/**
 *
 * @author tjd
 */
@ApplicationScoped
//@ManagedBean(eager = true)
@Named("applicationScope")
public class ApplicationScopeBean {

    private static final Logger LOGGER = Logger.getLogger(ApplicationScopeBean.class.getName());
    private static final String CATALOG_DATA = "/data/catalog.xml";

    @Inject
    private CatalogBean catalogBean;

    @PostConstruct
    public void init(){
        LOGGER.log(Level.INFO, "TOFO init catalog");
       catalogBean.addBookDTO(new BookDTO("Dr. Danny Coward", Book.Binding.Paperback, "0071837345", 512, new BigDecimal("50.00"), 2014, "McGraw-Hill Osborne Media", "Java EE 7: The Big Picture"));
        //BookDTO book1 = new BookDTO("Antonio Goncalves", Book.Binding.Paperback, "143024626X", 608, new BigDecimal("49.99"), 2013, "Apress", "Beginning Java EE 7 (Expert Voice in Java)");
        //addBookDTO(book1);
        /*for (BookDTO book : XmlParser.parse(CATALOG_DATA, BookDTO.class)) {
            addBookDTO(book);
        }*/
    }
   /* private void addBookDTO(BookDTO book) {
        try {
            catalogService.addBook(book);
        } catch (BookAlreadyExistsException ex) {
            LOGGER.log(Level.WARNING, "Book already exist", ex);
        }
    }*/
}