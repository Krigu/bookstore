package org.books.data.dao;

import org.apache.log4j.Logger;
import org.books.data.dao.generic.GenericDAOImpl;
import org.books.data.dto.BookInfo;
import org.books.data.entity.Book;
import org.books.data.entity.Book_;

import javax.ejb.Stateless;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h1>BookDAOBean</h1>
 * <p>
 * class DAO for the entity Book
 * </p>
 **/
@Stateless
public class BookDAOBean extends GenericDAOImpl<Book> implements BookDAOLocal {

    private static final Logger LOGGER = Logger.getLogger(BookDAOBean.class);

    public BookDAOBean() {
        super(Book.class);
    }

    @Override
    public Book find(String isbn) throws EntityNotFoundException {
        LOGGER.info("Find book by isbn : " + isbn);
        TypedQuery<Book> query = entityManager.createNamedQuery(Book.FIND_BY_ISBN, Book.class);
        query.setParameter("isbn", isbn);
        try {
            return query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
    }

    @Override
    public List<BookInfo> search(String keywords) {
        LOGGER.info("Search books by keywords : " + keywords);
        List<String> keywordList = getKeywordsAsList(keywords);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookInfo> cq = cb.createQuery(BookInfo.class);
        Root<Book> book = cq.from(Book.class);
        Predicate where = cb.conjunction();

        for (String keyword : keywordList) {
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

    private List<String> getKeywordsAsList(String keywords) {
        List<String> list = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(keywords);
        while (m.find()) {
            list.add(m.group(1).replace("\"", ""));
        }

        return list;
    }

}
