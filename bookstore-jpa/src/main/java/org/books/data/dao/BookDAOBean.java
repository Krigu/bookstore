/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.data.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityNotFoundException;
import org.apache.log4j.Logger;
import org.books.data.dao.generic.GenericDAOImpl;
import org.books.data.dto.BookInfo;
import org.books.data.entity.Book;

/**
 * <h1>BookDAOBean</h1>
 * <p>
 * class DAO for the entity Book
 * </p>
 *
 * @author Thomas Jeanmonod
 *
 **/
@Stateless
public class BookDAOBean extends GenericDAOImpl<Book> implements BookDAOLocal{

    private static final Logger LOGGER = Logger.getLogger(BookDAOBean.class);
    
    public BookDAOBean() {
        super(Book.class);
    }

    @Override
    public Book find(String isbn) throws EntityNotFoundException {
        //TODO
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BookInfo> search(String keywords) {
        //TODO
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
