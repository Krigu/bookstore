package org.books.presentation.bean.account;

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
    
    public static final String ORDER_NOT_FOUND = "org.books.presentation.bean.account.accountorderbean.ORDER_NOT_FOUND";
    public static final String ORDER_ALREADY_SHIPPED="org.books.presentation.bean.account.accountorderbean.ORDER_ALREADY_SHIPPED";

    private static final Logger LOGGER = Logger.getLogger(AccountOrderBean.class.getName());
    
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
            this.completedOrders = orderService.searchOrders(customerBean.getCustomer().getNumber(), selectedYear);
        } catch (CustomerNotFoundException ex) {
            MessageFactory.error(CustomerBean.CUSTOMER_NOT_FOUND);
            LOGGER.log(Level.WARNING, "Customer not found", ex);
        }
        
        if(completedOrders.isEmpty()){
            MessageFactory.info(ORDER_NOT_FOUND);
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
            orderService.cancelOrder(number);
        } catch (OrderNotFoundException ex) {
            LOGGER.log(Level.WARNING, "order not found", ex);
            MessageFactory.error(ORDER_NOT_FOUND);
        } catch (OrderAlreadyShippedException ex) {
            LOGGER.log(Level.WARNING, "Order already shipped", ex);
            MessageFactory.error(ORDER_ALREADY_SHIPPED);
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
            selectedOrder = orderService.findOrder(order.getNumber());
        } catch (OrderNotFoundException ex) {
            LOGGER.log(Level.WARNING, "Order not found", ex);
            MessageFactory.error(ORDER_NOT_FOUND);
            return null;
        }

        return "orderDetails";
    }

}
