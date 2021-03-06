package org.books.data.dao;

import org.books.data.dao.generic.GenericDAO;
import org.books.data.dto.OrderInfo;
import org.books.data.entity.Customer;
import org.books.data.entity.Order;

import javax.ejb.Local;
import javax.persistence.EntityNotFoundException;
import java.util.List;
/**
 * <h1>OrderDAOLocal</h1>
 * <p>
 * Interface for the Order DAO layer
 * </p>
 **/
@Local
public interface OrderDAOLocal extends GenericDAO<Order> {

    /**
     * <h1>T find(String number)</h1>
     *
     * <p>
     * an order with a particular number
     * </p>
     *
     * @param number number of the order
     * @exception EntityNotFoundException happen if the order isn't find
     *
     * @return the found entity instance
     */
    public Order find(String number);

    /**
     * <h1>List<OrderInfo> search(Customer customer, int year)</h1>
     *
     * <p>
     * information on all orders of a particular customer and a particular year
     * </p>
     *
     * @param customer customer of the orders
     * @param year year of the orders
     *
     * @return A list of founded orderInfo
     */
    public List<OrderInfo> search(Customer customer, int year);
}
