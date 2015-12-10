package org.books.data.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityNotFoundException;
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
 *
 * @author Thomas Jeanmonod
 *
 **/
@Stateless
public class OrderDAOBean extends GenericDAOImpl<Order> implements OrderDAOLocal {

    private static final Logger LOGGER = Logger.getLogger(OrderDAOBean.class);

    public OrderDAOBean() {
        super(Order.class);
    }

    @Override
    public Order find(String number) throws EntityNotFoundException {
        //TODO
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<OrderInfo> search(Customer customer, int year) {
        //TODO
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
