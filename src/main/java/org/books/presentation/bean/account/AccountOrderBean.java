package org.books.presentation.bean.account;

import org.books.application.Bookstore;
import org.books.application.BookstoreException;
import org.books.data.dto.OrderInfo;
import org.books.util.MessageFactory;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import org.books.data.dto.OrderDTO;

@SessionScoped
@Named("accountOrderBean")
public class AccountOrderBean implements Serializable {
    
    public static final String NO_ORDER_FOUND = "org.books.presentation.bean.account.accountorderbean.NO_ORDER_FOUND";

    @Inject
    private Bookstore bookstore;
    
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
            this.completedOrders = bookstore.searchOrders(customerBean.getCustomer().getEmail(), selectedYear);
        } catch (BookstoreException e) {
            // TODO
            MessageFactory.error(e.getMessage());
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
            bookstore.cancelOrder(number);
            displayOrderHistory();
        } catch (BookstoreException e) {
            MessageFactory.error(e.getMessage());
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
            selectedOrder = bookstore.findOrder(order.getNumber());
        } catch (BookstoreException ex) {
            return null;
        }

        return "orderDetails";
    }

}
