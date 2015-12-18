package org.books.data.dao;

import javax.ejb.Stateless;
import org.books.data.dao.generic.GenericDAOImpl;
import org.books.data.entity.OrderItem;

/**
 * <h1>OrderItemDAOBean</h1>
 * <p>
 * class DAO for the entity OrderItem
 * </p>
 *
 * @author Thomas Jeanmonod
 *
 **/
@Stateless
public class OrderItemDAOBean extends GenericDAOImpl<OrderItem> implements OrderItemDAOLocal {

    public OrderItemDAOBean() {
        super(OrderItem.class);
    }
    
}
