package org.books.data.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;
import org.books.data.dao.generic.GenericDAOImpl;
import org.books.data.dto.OrderInfo;
import org.books.data.entity.Customer;
import org.books.data.entity.Order;

/**
 * <h1>OrderDAOBean</h1>
 * <p>
 * class DAO for the entity Order
 * </p>
 */
@Stateless
public class OrderDAOBean extends GenericDAOImpl<Order> implements OrderDAOLocal {

    private static final Logger LOGGER = Logger.getLogger(OrderDAOBean.class);

    public OrderDAOBean() {
        super(Order.class);
    }

    @Override
    public Order find(String number){
        LOGGER.info("Find order by number : "+number);
        TypedQuery<Order> query = entityManager.createNamedQuery(Order.FIND_BY_NUMBER, Order.class);
        query.setParameter("number", number);
        try {
            return query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
    }

    @Override
    public List<OrderInfo> search(Customer customer, int year) {
        LOGGER.info("Search orderinfo by customer : "+customer.getId() +" and by year : "+year);
        TypedQuery<OrderInfo> query = entityManager.createNamedQuery(Order.FIND_BY_CUSTOMER_AND_YEAR, OrderInfo.class);
        query.setParameter("customer", customer);
        query.setParameter("year", year);
        return query.getResultList();
    }

}
