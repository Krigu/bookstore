package org.books.data.dao;

import javax.ejb.Local;
import org.books.data.dao.generic.GenericDAO;
import org.books.data.entity.OrderItem;

/**
 * <h1>OrderItemDAOLocal</h1>
 * <p>
 * Interface for the OrderItem DAO layer
 * </p>
 **/
@Local
public interface OrderItemDAOLocal extends GenericDAO<OrderItem>{
    
}
