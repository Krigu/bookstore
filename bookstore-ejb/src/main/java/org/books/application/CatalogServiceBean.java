package org.books.application;

import org.books.application.exception.BookAlreadyExistsException;
import org.books.application.exception.BookNotFoundException;
import org.books.application.interceptor.ValidationInterceptor;
import org.books.data.dao.BookDAOLocal;
import org.books.data.dto.BookDTO;
import org.books.data.dto.BookInfo;
import org.books.data.entity.Book;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "CatalogService")
@Interceptors(ValidationInterceptor.class)
public class CatalogServiceBean implements CatalogService {

    private static final Logger logger = Logger.getLogger(CatalogServiceBean.class.getName());

    @EJB
    private BookDAOLocal bookDAO;

    @Override
    public void addBook(BookDTO book) throws BookAlreadyExistsException {
        logger.log(Level.INFO, "Adding book with isbn ''{0}''", book.getIsbn());
        if (bookDAO.find(book.getIsbn()) != null) {
            throw new BookAlreadyExistsException();
        }
        bookDAO.create(new Book(book.getIsbn(), book.getTitle(), book.getAuthors(), book.getPublisher(), book.getPublicationYear(), book.getBinding(), book.getNumberOfPages(), book.getPrice()));
    }

    @Override
    public BookDTO findBook(String isbn) throws BookNotFoundException {
        logger.log(Level.INFO, "Finding book with isbn ''{0}''", isbn);
        Book book = bookDAO.find(isbn);
        if (book == null) {
            throw new BookNotFoundException();
        }
        return new BookDTO(book.getAuthors(), book.getBinding(), book.getIsbn(), book.getNumberOfPages(), book.getPrice(), book.getPublicationYear(), book.getPublisher(), book.getTitle());
    }

    @Override
    public List<BookInfo> searchBooks(String keywords) {
        logger.log(Level.INFO, "Search books for keywords ''{0}''", keywords);
        return bookDAO.search(keywords);
    }

    @Override
    public void updateBook(BookDTO book) throws BookNotFoundException {
        logger.log(Level.INFO, "Update books with isbn ''{0}''", book.getIsbn());
        Book bookEntity = bookDAO.find(book.getIsbn());
        
        if (bookEntity == null) {
            throw new BookNotFoundException();
        }
        
        bookEntity.setAuthors(book.getAuthors());
        bookEntity.setBinding(book.getBinding());
        bookEntity.setNumberOfPages(book.getNumberOfPages());
        bookEntity.setPrice(book.getPrice());
        bookEntity.setPublicationYear(book.getPublicationYear());
        bookEntity.setPublisher(book.getPublisher());
        bookEntity.setTitle(book.getTitle());
        
        bookDAO.update(bookEntity);
    }
    
}
