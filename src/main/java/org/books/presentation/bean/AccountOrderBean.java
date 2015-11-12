package org.books.presentation.bean;


import org.books.application.Bookstore;
import org.books.application.BookstoreException;
import org.books.application.ShoppingCart;
import org.books.data.dto.OrderInfo;
import org.books.util.MessageFactory;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SessionScoped
@Named("accountOrderBean")
public class AccountOrderBean implements Serializable {

    @Inject
    private Bookstore bookstore;

    @Inject
    private ShoppingCart shoppingCart;

    private List<OrderInfo> completedOrders = new ArrayList<>();

    private Integer selectedYear;

    public void displayOrderHistory(String email) {

        if (selectedYear == null || email == null)
            return;

        try {
            this.completedOrders = bookstore.searchOrders(email, selectedYear);
        } catch (BookstoreException e) {
            // TODO
            MessageFactory.error(e.getMessage());
        }
    }

    /**
     * Bricht eine Bestellung ab
     *
     * @param number Bestellnummer
     * @param email  E-Mail des Account für den refresh
     * @return
     */
    public String cancelOrder(String number, String email) {

        try {
            bookstore.cancelOrder(number);
            displayOrderHistory(email);
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
}
