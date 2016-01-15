package org.books.data.dao;

import org.books.data.dao.generic.GenericDAO;
import org.books.data.dto.BookInfo;
import org.books.data.entity.Book;

import javax.ejb.Local;
import java.util.List;

/**
 * <h1>BookDAOLocal</h1>
 * <p>
 * Interface for the Book DAO layer
 * </p>
 */
@Local
public interface BookDAOLocal extends GenericDAO<Book> {

    /**
     * <h1>T find(String numisbnber)</h1>
     *
     * <p>
     * a book with a particular ISBN number
     * </p>
     *
     * @param isbn emaisbnil of the book
     *
     * @return the found entity instance, otherwise null
     */
    public Book find(String isbn) ;

    /**
     * <h1>List<BookInfo> search(String keywords)</h1>
     *
     * <p>
     * all the books whose title, authors or publisher fields contain particular
     * keywords (all the keywords must be contained but not necessarily in the
     * same field)
     * </p>
     *
     * @param keywords 
     *
     * @return A list of founded BookInfo
     */
    public List<BookInfo> search(String keywords);
}
