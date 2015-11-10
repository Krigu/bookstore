package org.books.presentation.bean;


import org.books.application.Bookstore;
import org.books.application.BookstoreException;
import org.books.application.ShoppingCart;
import org.books.data.dto.OrderDTO;
import org.books.data.dto.OrderInfo;
import org.books.data.dto.OrderItemDTO;
import org.books.util.MessageFactory;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@SessionScoped
@Named("orderBean")
public class OrderBean implements Serializable {

    @Inject
    private Bookstore bookstore;

    @Inject
    private ShoppingCart shoppingCart;

    private OrderDTO order;

    /**
     *
     * @param email
     * @return
     */
    public String doSubmitOrder(String email, List<OrderItemDTO> orderItems) {

        try {
            this.order = bookstore.placeOrder(email, orderItems);
        } catch (BookstoreException e) {
            MessageFactory.error(e.getMessage());

            return null;
        }

        clearCart();

        return "/user/orderConfirmation";
    }

    private void clearCart() {
        shoppingCart.getItems().clear();
    }

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }
}
