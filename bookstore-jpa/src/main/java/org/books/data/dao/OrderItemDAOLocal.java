package org.books.data.dao;

import org.books.data.dao.generic.GenericDAO;
import org.books.data.entity.OrderItem;

import javax.ejb.Local;

/**
 * <h1>OrderItemDAOLocal</h1>
 * <p>
 * Interface for the OrderItem DAO layer
 * </p>
 **/
@Local
public interface OrderItemDAOLocal extends GenericDAO<OrderItem>{
    
}
