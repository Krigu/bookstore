package org.books.application;

import org.books.application.exception.*;
import org.books.data.dto.OrderDTO;
import org.books.data.dto.OrderInfo;
import org.books.data.dto.OrderItemDTO;

import javax.ejb.Remote;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author tjd
 */
@Remote
public interface OrderService {

    /**
     * Cancels an order.
     *
     * @param orderNr the number of the order
     * @throws OrderNotFoundException       if no order with the specified number exists
     * @throws OrderAlreadyShippedException if the order has already been shipped
     */
    void cancelOrder(@NotNull String orderNr) throws OrderNotFoundException, OrderAlreadyShippedException;

    /**
     * Finds an order by number.
     *
     * @param orderNr the number of the order
     * @return the data of the found order
     * @throws OrderNotFoundException if no order with the specified number exists
     */
    OrderDTO findOrder(@NotNull String orderNr) throws OrderNotFoundException;

    /**
     * Places an order on the bookstore.
     *
     * @param customerNr the number of the customer
     * @param items      the order items
     * @return the data of the placed order
     * @throws CustomerNotFoundException if no customer with the specified number exists
     * @throws BookNotFoundException     if an order item references a book that does not exist
     * @throws PaymentFailedException    if a payment error occurs
     */
    OrderDTO placeOrder(@NotNull String customerNr, @NotNull List<OrderItemDTO> items) throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException;

    /**
     * Searches for orders by customer and year.
     *
     * @param customerNr the email address of the customer
     * @param year       the year of the orders
     * @return
     * @throws CustomerNotFoundException if no customer with the specified number exists
     */
    List<OrderInfo> searchOrders(@NotNull String customerNr, @NotNull Integer year) throws CustomerNotFoundException;


}
