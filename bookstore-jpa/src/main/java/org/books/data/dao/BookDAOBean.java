/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.data.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
        String[] keywordArr = keywords.split("\\s+");
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookInfo> cq = cb.createQuery(BookInfo.class);
        Root<Book> book = cq.from(Book.class);
        Predicate where = cb.conjunction();
        
        for (String keyword: keywordArr) {
            String keywordUpper = keyword.toUpperCase();
            
            where = cb.and(where, cb.or(
                    cb.like(cb.upper(book.get("title").as(String.class)), "%" + keywordUpper + "%"),
                    cb.like(cb.upper(book.get("authors").as(String.class)), "%" + keywordUpper + "%"), 
                    cb.like(cb.upper(book.get("publisher").as(String.class)), "%" + keywordUpper + "%")
            ));
        }
        
        cq.where(where);
        cq.multiselect(book.get("isbn"), book.get("title"), book.get("price"));
        TypedQuery<BookInfo> q = entityManager.createQuery(cq);

        return q.getResultList();
    }
    
}
