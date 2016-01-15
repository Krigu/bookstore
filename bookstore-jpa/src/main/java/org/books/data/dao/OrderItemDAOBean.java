package org.books.data.dao;

import org.books.data.dao.generic.GenericDAOImpl;
import org.books.data.entity.OrderItem;

import javax.ejb.Stateless;

/**
 * <h1>OrderItemDAOBean</h1>
 * <p>
 * class DAO for the entity OrderItem
 * </p>
 **/
@Stateless
public class OrderItemDAOBean extends GenericDAOImpl<OrderItem> implements OrderItemDAOLocal {

    public OrderItemDAOBean() {
        super(OrderItem.class);
    }
    
}
