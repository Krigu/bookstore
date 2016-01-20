package org.books.presentation.bean.account;

/*import org.books.application.Bookstore;
import org.books.application.BookstoreException;
import org.books.data.dto.OrderDTO;
import org.books.data.dto.OrderInfo;*/
import org.books.util.MessageFactory;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import org.books.application.OrderService;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.OrderAlreadyShippedException;
import org.books.application.exception.OrderNotFoundException;
import org.books.data.dto.OrderDTO;
import org.books.data.dto.OrderInfo;

@SessionScoped
@Named("accountOrderBean")
public class AccountOrderBean implements Serializable {
    
    public static final String NO_ORDER_FOUND = "org.books.presentation.bean.account.accountorderbean.NO_ORDER_FOUND";

    private static final Logger LOGGER = Logger.getLogger(AccountOrderBean.class.getName());
    
    /*@Inject
    private Bookstore bookstore;*/
    @EJB
    private OrderService orderService;
    
    @Inject
    private CustomerBean customerBean;

    private List<OrderInfo> completedOrders = new ArrayList<>();

    private Integer selectedYear;

    private OrderDTO selectedOrder;

    public void displayOrderHistory() {

        if (selectedYear == null) {
            return;
        }

        try {
            /*try {
            this.completedOrders = bookstore.searchOrders(customerBean.getCustomer().getEmail(), selectedYear);
            
            } catch (BookstoreException e) {
            // TODO
            MessageFactory.error(e.getMessage());
            }*/
            this.completedOrders = orderService.searchOrders(customerBean.getCustomer().getNumber(), selectedYear);
        } catch (CustomerNotFoundException ex) {
            MessageFactory.error(CustomerBean.CUSTOMER_NOT_FOUND);
            LOGGER.log(Level.WARNING, "Customer not found", ex);
        }
        
        if(completedOrders.isEmpty()){
            MessageFactory.info(NO_ORDER_FOUND);
        }
    }

    /**
     * Bricht eine Bestellung ab
     *
     * @param number Bestellnummer
     * @return
     */
    public String cancelOrder(String number) {

        try {
            /*try {
            bookstore.cancelOrder(number);
            displayOrderHistory();
            } catch (BookstoreException e) {
            MessageFactory.error(e.getMessage());
            }*/
            orderService.cancelOrder(number);
        } catch (OrderNotFoundException ex) {
            //TODO
            LOGGER.log(Level.WARNING, "order not found", ex);
            MessageFactory.error(ex.getMessage());
        } catch (OrderAlreadyShippedException ex) {
            //TODO
            LOGGER.log(Level.WARNING, "Order allready shipped", ex);
            MessageFactory.error(ex.getMessage());
        }

        return null;
    }

    public List<OrderInfo> getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(List<OrderInfo> completedOrders) {
        this.completedOrders = completedOrders;
    }

    public Integer getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(Integer selectedYear) {
        this.selectedYear = selectedYear;
    }

    public OrderDTO getSelectedOrder() {
        return selectedOrder;
    }

    public String showOrderDetails(OrderInfo order) {
        try {
            /*try {
            selectedOrder = bookstore.findOrder(order.getNumber());
            } catch (BookstoreException ex) {
            return null;
            }*/
            selectedOrder = orderService.findOrder(order.getNumber());
        } catch (OrderNotFoundException ex) {
            LOGGER.log(Level.WARNING, "Order not found", ex);
            MessageFactory.error(ex.getMessage());
            return null;
        }

        return "orderDetails";
    }

}
