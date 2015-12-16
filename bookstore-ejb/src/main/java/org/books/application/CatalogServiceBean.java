package org.books.application;

import org.books.application.exception.BookAlreadyExistsException;
import org.books.application.exception.BookNotFoundException;
import org.books.data.dao.BookDAOBean;
import org.books.data.entity.Book;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "CatalogService")
public class CatalogServiceBean implements CatalogService {

    private static final Logger logger = Logger.getLogger(CatalogServiceBean.class.getName());

    @EJB
    private BookDAOBean bookDAO;

    @Override
    public void addBook(Book book) throws BookAlreadyExistsException {
        logger.log(Level.INFO, "Adding book with isbn ''{0}''", book.getIsbn());
        bookDAO.create(book);
    }

    @Override
    public Book findBook(String isbn) throws BookNotFoundException {
        logger.log(Level.INFO, "Finding book with isbn ''{0}''", isbn);
        Book book = bookDAO.find(isbn);
        if (book == null) {
            throw new BookNotFoundException();
        }
        return book;
    }
}
