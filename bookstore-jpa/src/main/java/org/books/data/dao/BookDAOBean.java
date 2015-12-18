package org.books.data.dao;

import org.apache.log4j.Logger;
import org.books.data.dao.generic.GenericDAOImpl;
import org.books.data.dto.BookInfo;
import org.books.data.entity.Book;
import org.books.data.entity.Book_;

import javax.ejb.Stateless;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * <h1>BookDAOBean</h1>
 * <p>
 * class DAO for the entity Book
 * </p>
 **/
@Stateless
public class BookDAOBean extends GenericDAOImpl<Book> implements BookDAOLocal{

    private static final Logger LOGGER = Logger.getLogger(BookDAOBean.class);
    
    public BookDAOBean() {
        super(Book.class);
    }

    @Override
    public Book find(String isbn) throws EntityNotFoundException {
        LOGGER.info("Find book by isbn : "+isbn);
        TypedQuery<Book> query = entityManager.createNamedQuery(Book.FINB_BY_ISBN, Book.class);
        query.setParameter("isbn", isbn);
        return query.getSingleResult();
    }

    @Override
    public List<BookInfo> search(String keywords) {
        LOGGER.info("Search books by keywords : "+keywords);
        String[] keywordArr = keywords.split("\\s+");
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookInfo> cq = cb.createQuery(BookInfo.class);
        Root<Book> book = cq.from(Book.class);
        Predicate where = cb.conjunction();
        
        for (String keyword: keywordArr) {
            String keywordUpper = keyword.toUpperCase();
            
            where = cb.and(where, cb.or(
                    cb.like(cb.upper(book.get(Book_.title)), "%" + keywordUpper + "%"),
                    cb.like(cb.upper(book.get(Book_.authors)), "%" + keywordUpper + "%"),
                    cb.like(cb.upper(book.get(Book_.publisher)), "%" + keywordUpper + "%")
            ));
        }
        
        cq.where(where);
        cq.multiselect(book.get(Book_.isbn), book.get(Book_.title), book.get(Book_.price));
        TypedQuery<BookInfo> q = entityManager.createQuery(cq);

        return q.getResultList();
    }
    
}
